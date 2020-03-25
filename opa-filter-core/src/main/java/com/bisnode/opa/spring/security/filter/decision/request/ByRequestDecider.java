package com.bisnode.opa.spring.security.filter.decision.request;

import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.QueryForDocumentRequest;
import com.bisnode.opa.spring.security.filter.decision.AccessDecider;
import com.bisnode.opa.spring.security.filter.decision.AccessDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is an {@link AccessDecider} that uses {@link HttpServletRequest} to ask OPA for decision.
 * It uses {@link OpaRequestSupplier} to build object used as OPA's {@code input}.
 */
public class ByRequestDecider implements AccessDecider<HttpServletRequest> {

    private static final Logger log = LoggerFactory.getLogger(ByRequestDecider.class);
    private final OpaQueryApi opaQueryApi;
    private final String documentPath;
    private final OpaRequestSupplier<?> requestSupplier;

    /**
     * Creates {@link ByRequestDecider} instance with {@link DefaultOpaRequestSupplier}.
     * @param opaQueryApi     Implementation of OPA client, see {@link com.bisnode.opa.client.OpaClient}.
     * @param documentPath    Path to OPA document that should be evaluated. It's 'package' in policy file. E.g. 'http/request/authz'.
     * @return New {@link ByRequestDecider} instance.
     */
    public static ByRequestDecider withDefaultSupplier(OpaQueryApi opaQueryApi, String documentPath) {
        return new ByRequestDecider(opaQueryApi, documentPath, new DefaultOpaRequestSupplier());
    }

    /**
     * Creates new {@link ByRequestDecider} instance.
     *
     * @param opaQueryApi     Implementation of OPA client, see {@link com.bisnode.opa.client.OpaClient}.
     * @param documentPath    Path to OPA document that should be evaluated. It's 'package' in policy file. E.g. 'http/request/authz'.
     * @param requestSupplier Supplier of OPA request, specifies concrete "input" contents in OPA evaluation.
     */
    public ByRequestDecider(@NonNull OpaQueryApi opaQueryApi, @NonNull String documentPath, OpaRequestSupplier<?> requestSupplier) {
        this.opaQueryApi = opaQueryApi;
        this.documentPath = documentPath;
        this.requestSupplier = requestSupplier;
    }

    /**
     * Asks OPA for decision using provided {@link OpaRequestSupplier}.
     * OPA's response is returned without interpretation.
     * Access is denied when OPA fails to respond.
     *
     * @param request Original HTTP request.
     * @return {@link AccessDecision} returned by OPA.
     */
    @Override
    public AccessDecision decideFor(HttpServletRequest request) {
        QueryForDocumentRequest queryForDocumentRequest = new QueryForDocumentRequest(requestSupplier.get(request), documentPath);
        log.trace("Asking OPA for access to {} {}", request.getMethod(), request.getServletPath());
        return opaQueryApi.queryForDocument(queryForDocumentRequest, AccessDecision.class);
    }
}
