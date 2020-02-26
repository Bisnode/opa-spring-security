package com.bisnode.opa.spring.security


import com.bisnode.opa.client.query.OpaQueryApi
import com.bisnode.opa.client.query.QueryForDocumentRequest
import com.bisnode.opa.spring.security.filter.OpaFilter
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest

class OpaFilterSpec extends Specification {

    OpaQueryApi opaQueryApi = Mock()

    @Subject
    OpaFilter opaFilter = new OpaFilter(opaQueryApi, SOME_POLICY_NAME)

    def 'should throw AccessDeniedException on disallow'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilter.Decision) >> decisionWithAllow(false)

        when:
          opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
          thrown AccessDeniedException
    }

    def 'should throw AccessDeniedException on undefined response'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilter.Decision) >> decisionWithAllow(null)

        when:
          opaFilter.doFilter(httpServletRequest, null, Mock(FilterChain))

        then:
          thrown AccessDeniedException
    }


    def 'should continue filtering on allow'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilter.Decision) >> decisionWithAllow(true)

        when:
          opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
          1 * filterChain.doFilter(httpServletRequest, _)
    }

    static OpaFilter.Decision decisionWithAllow(Boolean allow) {
        OpaFilter.Decision decision = new OpaFilter.Decision()
        decision.allow = allow
        return decision
    }

    static final String SOME_POLICY_NAME = 'somePolicy'

}
