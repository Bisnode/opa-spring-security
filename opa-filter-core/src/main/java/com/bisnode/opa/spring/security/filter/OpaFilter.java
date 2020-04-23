package com.bisnode.opa.spring.security.filter;

import com.bisnode.opa.client.OpaClientException;
import com.bisnode.opa.spring.security.filter.decision.AccessDecider;
import com.bisnode.opa.spring.security.filter.decision.AccessDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class OpaFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(OpaFilter.class);

    private final AccessDecider<HttpServletRequest> decider;
    private final ApplicationEventPublisher eventPublisher;
    private final RequestMatcher whitelistRequestMatcher;

    /**
     * Creates new {@link OpaFilter} instance.
     *
     * @param decider                 {@link AccessDecider} to use in {@link OpaFilter#doFilter(ServletRequest, ServletResponse, FilterChain)} method.
     * @param eventPublisher          {@link ApplicationEventPublisher} to publish events when deny occurs
     * @param whitelistRequestMatcher {@link RequestMatcher} to exclude requests for OPA filtering
     */
    public OpaFilter(@NonNull AccessDecider<HttpServletRequest> decider,
                     ApplicationEventPublisher eventPublisher,
                     @NonNull RequestMatcher whitelistRequestMatcher) {
        this.decider = decider;
        this.eventPublisher = eventPublisher;
        this.whitelistRequestMatcher = whitelistRequestMatcher;
    }

    /**
     * Creates new {@link OpaFilter} instance.
     *
     * @param decider        {@link AccessDecider} to use in {@link OpaFilter#doFilter(ServletRequest, ServletResponse, FilterChain)} method.
     * @param eventPublisher {@link ApplicationEventPublisher} to publish events when deny occurs
     */
    public OpaFilter(@NonNull AccessDecider<HttpServletRequest> decider,
                     ApplicationEventPublisher eventPublisher) {
        this(decider, eventPublisher, request -> false);
    }

    /**
     * Creates new {@link OpaFilter} instance.
     *
     * @param decider {@link AccessDecider} to use in {@link OpaFilter#doFilter(ServletRequest, ServletResponse, FilterChain)} method.
     */
    public OpaFilter(@NonNull AccessDecider<HttpServletRequest> decider) {
        this(decider, event -> { }, request -> false);
    }

    /**
     * Filters request using {@link AccessDecider}.
     * If OPA decides access should be denied {@link AccessDeniedException} is thrown
     * and {@link AuthorizationFailureEvent} is published, if publisher is provided.
     * Otherwise, the filtering is passed back to the chain.
     *
     * @param request  the {@link HttpServletRequest} object containing the client's request.
     * @param response unused.
     * @param chain    the {@link FilterChain} for invoking the next filter.
     * @throws IOException           if an I/O related error has occurred during the processing.
     * @throws ServletException      if an exception occurs that interferes with the
     *                               filter's normal operation.
     * @throws AccessDeniedException when OPA decides access should be denied or fails to respond.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            boolean whitelisted = whitelistRequestMatcher.matches(httpRequest);
            if (!whitelisted) {
                decideFor(httpRequest);
            } else {
                log.trace("Access allowed for whitelisted {} {}", httpRequest.getMethod(), httpRequest.getServletPath());
            }
        }
        chain.doFilter(request, response);
    }

    private void decideFor(HttpServletRequest httpRequest) {
        try {
            AccessDecision accessDecision = decider.decideFor(httpRequest);
            log.trace("OPA response is 'allow': {} for access to {} {}", accessDecision.getAllow(), httpRequest.getMethod(), httpRequest.getServletPath());
            if (!accessDecision.isAllow()) {
                denyAccess(accessDecision);
            }
        } catch (OpaClientException opaException) {
            logAndDeny(httpRequest, opaException);
        }
    }

    private void denyAccess(AccessDecision accessDecision) {
        String rejectionMessage = String.format("Access request rejected by OPA because: %s", accessDecision.getReason());
        log.debug(rejectionMessage);
        AccessDeniedException accessDeniedException = new AccessDeniedException(rejectionMessage);
        AuthorizationFailureEvent event = new AuthorizationFailureEvent(
                this, List.of(), SecurityContextHolder.getContext().getAuthentication(), accessDeniedException);
        eventPublisher.publishEvent(event);
        throw accessDeniedException;
    }

    private void logAndDeny(HttpServletRequest httpRequest, OpaClientException opaException) {
        String errorMessage = String.format("OpaClientException caught when requesting access to %s %s", httpRequest.getMethod(), httpRequest.getServletPath());
        log.warn(errorMessage, opaException);
        throw new AccessDeniedException(errorMessage, opaException);
    }

}
