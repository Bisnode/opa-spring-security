package com.bisnode.opa.spring.security.filter;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.client.query.QueryForDocumentRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.lang.Boolean.TRUE;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class OpaFilter extends GenericFilterBean {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OpaFilter.class);
    private final OpaQueryApi opaQueryApi;
    private final String policyName;

    public OpaFilter(OpaQueryApi opaQueryApi, String policyName) {
        this.opaQueryApi = opaQueryApi;
        this.policyName = policyName;
    }

    public static OpaFilterBuilder builder() {
        return new OpaFilterBuilder();
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
        OpaInput.OpaInputBuilder inputBuilder = OpaInput.builderFrom(request);
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

    @JsonInclude(NON_NULL)
    private static class OpaInput {
        private final String path;
        private final String method;
        private final String encodedJwt;

        public OpaInput(String path, String method, String encodedJwt) {
            this.path = path;
            this.method = method;
            this.encodedJwt = encodedJwt;
        }

        static OpaInputBuilder builderFrom(HttpServletRequest httpRequest) {
            return OpaInput.builder()
                    .method(httpRequest.getMethod())
                    .path(httpRequest.getServletPath());
        }

        public static OpaInputBuilder builder() {
            return new OpaInputBuilder();
        }

        public String getPath() {
            return this.path;
        }

        public String getMethod() {
            return this.method;
        }

        public String getEncodedJwt() {
            return this.encodedJwt;
        }

        public static class OpaInputBuilder {
            private String path;
            private String method;
            private String encodedJwt;

            OpaInputBuilder() {
            }

            public OpaInput.OpaInputBuilder path(String path) {
                this.path = path;
                return this;
            }

            public OpaInput.OpaInputBuilder method(String method) {
                this.method = method;
                return this;
            }

            public OpaInput.OpaInputBuilder encodedJwt(String encodedJwt) {
                this.encodedJwt = encodedJwt;
                return this;
            }

            public OpaInput build() {
                return new OpaInput(path, method, encodedJwt);
            }

            public String toString() {
                return "OpaFilter.OpaInput.OpaInputBuilder(path=" + this.path + ", method=" + this.method + ", encodedJwt=" + this.encodedJwt + ")";
            }
        }
    }

    private static class Decision {
        private Boolean allow;
        private String reason;

        boolean isAllow() {
            return TRUE.equals(allow);
        }

        public Boolean getAllow() {
            return this.allow;
        }

        public String getReason() {
            return this.reason;
        }

        public void setAllow(Boolean allow) {
            this.allow = allow;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class OpaFilterBuilder {
        private OpaQueryApi opaQueryApi;
        private String policyName;

        OpaFilterBuilder() {
        }

        public OpaFilter.OpaFilterBuilder opaQueryApi(OpaQueryApi opaQueryApi) {
            this.opaQueryApi = opaQueryApi;
            return this;
        }

        public OpaFilter.OpaFilterBuilder policyName(String policyName) {
            this.policyName = policyName;
            return this;
        }

        public OpaFilter build() {
            return new OpaFilter(opaQueryApi, policyName);
        }

        public String toString() {
            return "OpaFilter.OpaFilterBuilder(opaQueryApi=" + this.opaQueryApi + ", policyName=" + this.policyName + ")";
        }
    }
}
