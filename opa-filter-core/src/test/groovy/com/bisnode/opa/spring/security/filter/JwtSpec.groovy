package com.bisnode.opa.spring.security.filter

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.oauth2.core.AbstractOAuth2Token
import spock.lang.Shared
import spock.lang.Specification

class JwtSpec extends Specification{

    @Shared
    SecurityContext contextWithMockOAuth2

    def setupSpec() {
        Authentication authentication = Stub() {
            getCredentials() >> Stub(AbstractOAuth2Token) {
                getTokenValue() >> 'some.token.withCrypto'
            }
        }

        contextWithMockOAuth2 = new SecurityContextImpl(authentication)
    }

    def 'should get token on oAuth2 authentication'() {
        given:
          SecurityContextHolder.setContext(contextWithMockOAuth2)

        when:
          Optional<Jwt> jwt = Jwt.fromSecurityContext()

        then:
          jwt.isPresent()
    }

    def 'should return empty on no authentication'() {
        given:
          SecurityContextHolder.setContext(new SecurityContextImpl(null))

        when:
          Optional<Jwt> jwt = Jwt.fromSecurityContext()

        then:
          jwt.isEmpty()

    }
}
