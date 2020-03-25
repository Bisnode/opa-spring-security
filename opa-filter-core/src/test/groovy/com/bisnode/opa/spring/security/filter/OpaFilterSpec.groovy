package com.bisnode.opa.spring.security.filter

import com.bisnode.opa.spring.security.filter.decision.AccessDecider
import com.bisnode.opa.spring.security.filter.decision.AccessDecision
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest

class OpaFilterSpec extends Specification {

    AccessDecider<HttpServletRequest> decider = Stub()

    @Subject
    OpaFilter opaFilter = new OpaFilter(decider)

    def 'should throw AccessDeniedException on disallow'() {
        given:
            FilterChain filterChain = Mock()
            HttpServletRequest httpServletRequest = Mock()
            decider.decideFor(httpServletRequest) >> new AccessDecision(allow: false)

        when:
            opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
            thrown AccessDeniedException
            0 * filterChain.doFilter(_, _)
    }

    def 'should throw AccessDeniedException on undefined response'() {
        given:
            FilterChain filterChain = Mock()
            HttpServletRequest httpServletRequest = Mock()
            decider.decideFor(httpServletRequest) >> new AccessDecision()

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

        when:
            opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
            1 * filterChain.doFilter(httpServletRequest, _)
            noExceptionThrown()
    }

}
