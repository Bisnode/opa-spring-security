package com.bisnode.opa.spring.security.filter;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.QueryForDocumentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class OpaFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(OpaFilter.class);
    private final OpaQueryApi opaQueryApi;
    private final String policyName;

    public OpaFilter(OpaQueryApi opaQueryApi, String policyName) {
        this.opaQueryApi = opaQueryApi;
        this.policyName = policyName;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("OpaFilter supports only HTTP requests");
        }
        decideFor((HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    private void decideFor(HttpServletRequest httpRequest) {
        try {
            Decision decision = fetchDecision(httpRequest);
            log.info("OPA response is 'allow': {} for access to {} {}", decision.getAllow(), httpRequest.getMethod(), httpRequest.getServletPath());
            if (!decision.isAllow()) {
                denyAccess(decision);
            }
        } catch (OpaClientException opaException) {
            logAndDeny(httpRequest, opaException);
        }
    }

    private Decision fetchDecision(HttpServletRequest request) {
        OpaInput.Builder inputBuilder = OpaInput.builderFrom(request);
        Jwt.fromSecurityContext().map(Jwt::getEncoded).ifPresent(inputBuilder::encodedJwt);

        QueryForDocumentRequest queryForDocumentRequest = new QueryForDocumentRequest(inputBuilder.build(), policyName);

        log.trace("Asking OPA for access to {} {}", request.getMethod(), request.getServletPath());
        return opaQueryApi.queryForDocument(queryForDocumentRequest, Decision.class);
    }

    private void denyAccess(Decision decision) {
        String rejectionMessage = String.format("Access request rejected by OPA because: %s", decision.getReason());
        log.info(rejectionMessage);
        throw new AccessDeniedException(rejectionMessage);

    }

    private void logAndDeny(HttpServletRequest httpRequest, OpaClientException opaException) {
        String errorMessage = String.format("OpaClientException caught when requesting access to %s %s", httpRequest.getMethod(), httpRequest.getServletPath());
        log.error(errorMessage, opaException);
        throw new AccessDeniedException(errorMessage, opaException);
    }

}
