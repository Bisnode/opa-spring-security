package com.bisnode.opa.spring.security.filter.decision;

import org.springframework.lang.Nullable;

import static java.lang.Boolean.TRUE;

/**
 * This class is used to map OPA's response to evaluation request.
 */
public class AccessDecision {
    private Boolean allow;
    private String reason;

    /**
     * Returns OPA's decision or {@code false} if OPA fails to respond.
     *
     * @return {@code true} if access should be allowed, {@code false} otherwise.
     */
    public boolean isAllow() {
        return TRUE.equals(allow);
    }

    /**
     * @return value of 'allow' field in OPA's response.
     */
    @Nullable
    public Boolean getAllow() {
        return this.allow;
    }

    /**
     * @return value of 'reason' field in OPA's response.
     */
    @Nullable
    public String getReason() {
        return this.reason;
    }

    void setAllow(@Nullable Boolean allow) {
        this.allow = allow;
    }

    void setReason(@Nullable String reason) {
        this.reason = reason;
    }
}
