package com.bisnode.opa.spring.security.filter.decision.request;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface used to supply {@link ByRequestDecider} with object used as input in OPA's evaluation.
 * @param <T> type of supplied object.
 */
@FunctionalInterface
public interface OpaRequestSupplier<T> {

    /**
     * @param request to deny or allow access to.
     * @return object to be used as OPA's {@code input}.
     */
    T get(HttpServletRequest request);
}
