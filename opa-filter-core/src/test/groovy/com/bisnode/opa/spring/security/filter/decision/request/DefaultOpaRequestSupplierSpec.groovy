package com.bisnode.opa.spring.security.filter.decision.request

import org.springframework.mock.web.MockHttpServletRequest
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest

class DefaultOpaRequestSupplierSpec extends Specification {

    @Subject
    DefaultOpaRequestSupplier defaultOpaRequestSupplier = new DefaultOpaRequestSupplier()

    def 'should fill request with method and path'() {
        given:
            HttpServletRequest httpServletRequest = new MockHttpServletRequest('GET', 'http://localhost:8080/test')
            httpServletRequest.setServletPath('/test')

        when:
            def input = defaultOpaRequestSupplier.get(httpServletRequest)

        then:
            input.properties['method'] == 'GET'
            input.properties['path'] == '/test'
    }

    def 'should not fill request with JWT when token is missing'() {
        given:
            HttpServletRequest httpServletRequest = new MockHttpServletRequest('GET', 'http://localhost:8080/test')

        when:
            def input = defaultOpaRequestSupplier.get(httpServletRequest)

        then:
            !input.properties['encodedJWT']
    }

    def 'should fill request with JWT when token is present'() {
        given:
            String token = 'some.token.withCrypto'

            HttpServletRequest httpServletRequest = new MockHttpServletRequest('GET', 'http://localhost:8080/test')
            httpServletRequest.addHeader('Authorization', "Bearer $token")

        when:
            def input = defaultOpaRequestSupplier.get(httpServletRequest)

        then:
            input.properties['encodedJwt'] == token
    }

}
