package com.bisnode.opa.spring.security.filter;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.QueryForDocumentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class OpaAccesDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    private static final Logger log = LoggerFactory.getLogger(OpaAccesDecisionVoter.class);
    private final OpaQueryApi opaQueryApi;

    public OpaAccesDecisionVoter(OpaQueryApi opaQueryApi, String policyName) {
        this.opaQueryApi = opaQueryApi;
        this.policyName = policyName;
    }

    private final String policyName;


    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        return decideFor(object.getHttpRequest());
    }

    private int decideFor(HttpServletRequest httpRequest) {
        try {
            Decision decision = fetchDecision(httpRequest);
            log.info("OPA response is 'allow': {} for access to {} {}", decision.getAllow(), httpRequest.getMethod(), httpRequest.getServletPath());
            if (!decision.isAllow()) {
                return denyAccess(decision);
            } else {
                return ACCESS_GRANTED;
            }
        } catch (OpaClientException opaException) {
            return logAndDeny(httpRequest, opaException);
        }
    }

    private Decision fetchDecision(HttpServletRequest request) {
        OpaInput.Builder inputBuilder = OpaInput.builderFrom(request);
        Jwt.fromSecurityContext().map(Jwt::getEncoded).ifPresent(inputBuilder::encodedJwt);

        QueryForDocumentRequest queryForDocumentRequest = new QueryForDocumentRequest(inputBuilder.build(), policyName);

        log.trace("Asking OPA for access to {} {}", request.getMethod(), request.getServletPath());
        return opaQueryApi.queryForDocument(queryForDocumentRequest, Decision.class);
    }

    private int denyAccess(Decision decision) {
        String rejectionMessage = String.format("Access request rejected by OPA because: %s", decision.getReason());
        log.info(rejectionMessage);
        return ACCESS_DENIED;
    }

    private int logAndDeny(HttpServletRequest httpRequest, OpaClientException opaException) {
        String errorMessage = String.format("OpaClientException caught when requesting access to %s %s", httpRequest.getMethod(), httpRequest.getServletPath());
        log.error(errorMessage, opaException);
        return ACCESS_DENIED;
    }

}
