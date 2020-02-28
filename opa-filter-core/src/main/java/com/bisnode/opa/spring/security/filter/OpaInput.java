package com.bisnode.opa.spring.security.filter;

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

        static Builder builder() {
            return new Builder();
        }

        String getPath() {
            return this.path;
        }

        String getMethod() {
            return this.method;
        }

        String getEncodedJwt() {
            return this.encodedJwt;
        }

        public static class Builder {
            private String path;
            private String method;
            private String encodedJwt;

            Builder() {
            }

            public Builder path(String path) {
                this.path = path;
                return this;
            }

            public Builder method(String method) {
                this.method = method;
                return this;
            }

            public Builder encodedJwt(String encodedJwt) {
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