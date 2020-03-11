package com.bisnode.opa.spring.security.filter;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

class OpaInput {
    private final String path;
    private final String method;
    private final String encodedJwt;

    private OpaInput(String path, String method, String encodedJwt) {
        this.path = path;
        this.method = method;
        this.encodedJwt = encodedJwt;
    }

    static Builder builderFrom(HttpServletRequest httpRequest) {
        return OpaInput.builder()
                .method(httpRequest.getMethod())
                .path(httpRequest.getServletPath());
    }

    @NonNull
    static Builder builder() {
        return new Builder();
    }

    @Nullable
    public String getPath() {
        return this.path;
    }

    @Nullable
    public String getMethod() {
        return this.method;
    }

    @Nullable
    public String getEncodedJwt() {
        return this.encodedJwt;
    }

    static class Builder {
        private String path;
        private String method;
        private String encodedJwt;

        Builder() {
        }

        Builder path(String path) {
            this.path = path;
            return this;
        }

        Builder method(String method) {
            this.method = method;
            return this;
        }

        Builder encodedJwt(String encodedJwt) {
            this.encodedJwt = encodedJwt;
            return this;
        }

        OpaInput build() {
            return new OpaInput(path, method, encodedJwt);
        }

        public String toString() {
            return "OpaFilter.OpaInput.OpaInputBuilder(path=" + this.path + ", method=" + this.method + ", encodedJwt=" + this.encodedJwt + ")";
        }
    }
}
