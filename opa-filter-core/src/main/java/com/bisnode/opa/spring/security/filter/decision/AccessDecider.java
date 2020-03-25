package com.bisnode.opa.spring.security.filter.decision;

@FunctionalInterface
public interface AccessDecider<T> {
    AccessDecision decideBy(T factor);
}
