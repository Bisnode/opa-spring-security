package com.bisnode.opa.spring.security.filter

import com.bisnode.opa.spring.security.filter.decision.AccessDecider
import com.bisnode.opa.spring.security.filter.decision.AccessDecision
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.event.AuthorizationFailureEvent
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest

class OpaFilterSpec extends Specification {

    AccessDecider<HttpServletRequest> decider = Stub()
    ApplicationEventPublisher eventPublisher = Mock()
    RequestMatcher whitelistRequestMatcher = Mock()
    SecurityContext securityContext = Mock()

    @Subject
    OpaFilter opaFilter = new OpaFilter(decider, eventPublisher, whitelistRequestMatcher)

    def setup() {
        securityContext.getAuthentication() >> Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)
    }

    def 'should throw AccessDeniedException on disallow and publish AuthorizationFailureEvent'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          decider.decideFor(httpServletRequest) >> new AccessDecision(allow: false)
          whitelistRequestMatcher.matches(_ as HttpServletRequest) >> false

        when:
          opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
          thrown AccessDeniedException
          0 * filterChain.doFilter(_, _)
          1 * eventPublisher.publishEvent(_ as AuthorizationFailureEvent)
    }

    def 'should throw AccessDeniedException on undefined response'() {
        given:
            FilterChain filterChain = Mock()
            HttpServletRequest httpServletRequest = Mock()
            decider.decideFor(httpServletRequest) >> new AccessDecision()
            whitelistRequestMatcher.matches(_ as HttpServletRequest) >> false

        when:
            opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
            thrown AccessDeniedException
            0 * filterChain.doFilter(_, _)
    }

    def 'should continue filtering on allow'() {
        given:
            FilterChain filterChain = Mock()
            HttpServletRequest httpServletRequest = Mock()
            decider.decideFor(httpServletRequest) >> new AccessDecision(allow: true)
            whitelistRequestMatcher.matches(_ as HttpServletRequest) >> false

        when:
            opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
            1 * filterChain.doFilter(httpServletRequest, _)
            noExceptionThrown()
    }

    def 'should continue filtering and do not call OPA when path whitelisted'() {
        given:
            FilterChain filterChain = Mock()
            HttpServletRequest httpServletRequest = Mock()
            whitelistRequestMatcher.matches(httpServletRequest) >> true

        when:
            opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
            0 * decider.decideFor(httpServletRequest)
            1 * filterChain.doFilter(httpServletRequest, _)
            noExceptionThrown()
    }

}
