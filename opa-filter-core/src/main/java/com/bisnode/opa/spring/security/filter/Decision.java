package com.bisnode.opa.spring.security.filter;

import static java.lang.Boolean.TRUE;

class Decision {
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
