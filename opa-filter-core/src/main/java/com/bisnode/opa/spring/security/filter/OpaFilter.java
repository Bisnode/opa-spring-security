package com.bisnode.opa.spring.security.filter;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.QueryForDocumentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
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
    private final String documentPath;

    /**
     * Creates new {@link OpaFilter} instance.
     *
     * @param opaQueryApi implementation of OPA client, see {@link com.bisnode.opa.client.OpaClient}.
     * @param documentPath path to OPA document that should be evaluated. It's 'package' in policy file. E.g. 'http/request/authz'.
     */
    public OpaFilter(@NonNull OpaQueryApi opaQueryApi, @NonNull String documentPath) {
        this.opaQueryApi = opaQueryApi;
        this.documentPath = documentPath;
    }

    /**
     * Filters incoming request in a way that it asks OPA if access should be allowed based on:
     * <ul>
     *     <li>request path ({@link HttpServletRequest#getServletPath()})</li>
     *     <li>request method ({@link HttpServletRequest#getMethod()} ()})</li>
     *     <li>encoded JWT taken from {@link org.springframework.security.core.context.SecurityContextHolder}</li>
     * </ul>
     * If OPA decides access should be denied {@link AccessDeniedException} is thrown. Otherwise, the filtering is passed back to the chain.
     * Access is denied also when OPA fails to respond.
     *
     * @param request the {@link HttpServletRequest} object containing the client's request.
     * @param response unused.
     * @param chain the {@link FilterChain} for invoking the next filter.
     * @throws IOException if an I/O related error has occurred during the processing.
     * @throws ServletException if an exception occurs that interferes with the
     *                          filter's normal operation.
     * @throws AccessDeniedException when OPA decides access should be denied or fails to respond.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            decideFor((HttpServletRequest) request);
        }
        chain.doFilter(request, response);
    }

    private void decideFor(HttpServletRequest httpRequest) {
        try {
            Decision decision = fetchDecision(httpRequest);
            log.trace("OPA response is 'allow': {} for access to {} {}", decision.getAllow(), httpRequest.getMethod(), httpRequest.getServletPath());
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

        QueryForDocumentRequest queryForDocumentRequest = new QueryForDocumentRequest(inputBuilder.build(), documentPath);

        log.trace("Asking OPA for access to {} {}", request.getMethod(), request.getServletPath());
        return opaQueryApi.queryForDocument(queryForDocumentRequest, Decision.class);
    }

    private void denyAccess(Decision decision) {
        String rejectionMessage = String.format("Access request rejected by OPA because: %s", decision.getReason());
        log.debug(rejectionMessage);
        throw new AccessDeniedException(rejectionMessage);
    }

    private void logAndDeny(HttpServletRequest httpRequest, OpaClientException opaException) {
        String errorMessage = String.format("OpaClientException caught when requesting access to %s %s", httpRequest.getMethod(), httpRequest.getServletPath());
        log.warn(errorMessage, opaException);
        throw new AccessDeniedException(errorMessage, opaException);
    }

}
