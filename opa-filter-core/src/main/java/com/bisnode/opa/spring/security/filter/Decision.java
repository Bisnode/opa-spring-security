package com.bisnode.opa.spring.security.filter;

import org.springframework.lang.Nullable;

import static java.lang.Boolean.TRUE;

class Decision {
    private Boolean allow;
    private String reason;

    boolean isAllow() {
        return TRUE.equals(allow);
    }

    @Nullable
    Boolean getAllow() {
        return this.allow;
    }

    @Nullable
    String getReason() {
        return this.reason;
    }

    void setAllow(@Nullable Boolean allow) {
        this.allow = allow;
    }

    void setReason(@Nullable String reason) {
        this.reason = reason;
    }
}
