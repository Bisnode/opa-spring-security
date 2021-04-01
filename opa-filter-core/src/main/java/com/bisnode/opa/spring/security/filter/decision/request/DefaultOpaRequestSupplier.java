package com.bisnode.opa.spring.security.filter.decision.request;

import javax.servlet.http.HttpServletRequest;

public class DefaultOpaRequestSupplier implements OpaRequestSupplier<OpaInput> {

    /**
     * Creates OPA request with following input:
     * <ul>
     *     <li>request path ({@link HttpServletRequest#getServletPath()})</li>
     *     <li>request method ({@link HttpServletRequest#getMethod()} ()})</li>
     *     <li>encoded JWT taken from {@link org.springframework.security.core.context.SecurityContextHolder}</li>
     * </ul>
     * @param request Request to extract data from.
     * @return Object representing request to OPA.
     */
    @Override
    public OpaInput get(HttpServletRequest request) {
        OpaInput.Builder inputBuilder = OpaInput.builder()
                .method(request.getMethod())
                .path(request.getServletPath());
        Jwt.fromHttpRequest(request).map(Jwt::getEncoded).ifPresent(inputBuilder::encodedJwt);
        return inputBuilder.build();
    }
}
