package com.bisnode.opa.spring.security

import com.bisnode.opa.client.OpaClientException
import com.bisnode.opa.client.query.OpaQueryApi
import com.bisnode.opa.client.query.QueryForDocumentRequest
import com.bisnode.opa.spring.security.filter.OpaFilter
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest

class OpaFilterSpec extends Specification{
    OpaQueryApi opaQueryApi = Mock()

    @Subject
    OpaFilterSpec opaFilter = new OpaFilterSpec(opaQueryApi, SOME_POLICY_NAME)

    def 'should throw AccessDeniedException on disallow'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilterSpec.Decision) >> decisionWithAllow(false)

        when:
          opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
          thrown AccessDeniedException
    }

    def 'should throw AccessDeniedException on undefined response'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilterSpec.Decision) >> decisionWithAllow(null)

        when:
          opaFilter.doFilter(httpServletRequest, null, Mock(FilterChain))

        then:
          thrown AccessDeniedException
    }


    def 'should continue filtering on allow'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilterSpec.Decision) >> decisionWithAllow(true)

        when:
          opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
          1 * filterChain.doFilter(httpServletRequest, _)
    }

    @Unroll
    def 'should not throw exception in dry run mode when decision is #shouldAllow'() {
        given:
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilterSpec.Decision) >> decisionWithAllow(shouldAllow)

          OpaFilterSpec opaFilter = OpaFilterSpec.builder()
                  .opaQueryApi(opaQueryApi)
                  .policyName(SOME_POLICY_NAME)
                  .dryRun(true)
                  .build()
        when:
          opaFilter.doFilter(Mock(HttpServletRequest), null, Mock(FilterChain))

        then:
          noExceptionThrown()

        where:
          shouldAllow << [Boolean.TRUE, Boolean.FALSE, null]

    }

    def 'should not throw exception in dry run mode when opa raises exception'() {
        given:
          FilterChain filterChain = Mock()
          HttpServletRequest httpServletRequest = Mock()
          opaQueryApi.queryForDocument(_ as QueryForDocumentRequest, OpaFilterSpec.Decision) >> { throw new OpaClientException() }

          OpaFilterSpec opaFilter = OpaFilterSpec.builder()
                  .opaQueryApi(opaQueryApi)
                  .policyName(SOME_POLICY_NAME)
                  .dryRun(true)
                  .build()
        when:
          opaFilter.doFilter(httpServletRequest, null, filterChain)

        then:
          noExceptionThrown()
          1 * filterChain.doFilter(httpServletRequest, _)
    }

    static OpaFilter.Decision decisionWithAllow(Boolean allow) {
        OpaFilter.Decision decision = new OpaFilter.Decision()
        decision.allow = allow
        return decision
    }

    static final String SOME_POLICY_NAME = 'somePolicy'
}
