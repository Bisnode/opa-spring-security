package com.bisnode.opa.spring.security.filter.decision.request

import org.springframework.mock.web.MockHttpServletRequest
import spock.lang.Specification

class JwtSpec extends Specification {

    def 'should get token from HTTP request with oAuth2 authentication header'() {
        given:
            def accessToken = 'some.token.withCrypto'
            def httpRequest = new MockHttpServletRequest()
            httpRequest.addHeader('Authorization', "Bearer $accessToken")

        when:
            Optional<Jwt> jwt = Jwt.fromHttpRequest(httpRequest)
            jwt.get().encoded == accessToken

        then:
            jwt.isPresent()
    }

    def 'should return empty when HTTP request without oAuth2 authentication header'() {
        given:
            def httpRequest = new MockHttpServletRequest()

        when:
            Optional<Jwt> jwt = Jwt.fromHttpRequest(httpRequest)

        then:
            jwt.isEmpty()
    }

    def 'should return empty when HTTP request is null'() {
        when:
            Optional<Jwt> jwt = Jwt.fromHttpRequest(null)

        then:
            jwt.isEmpty()
    }

}
