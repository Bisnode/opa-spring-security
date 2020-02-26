package com.bisnode.opa.spring.security

import com.bisnode.opa.spring.security.filter.Jwt
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import spock.lang.Shared
import spock.lang.Specification

class JwtSpec extends Specification{

    @Shared
    SecurityContext contextWithMockOAuth2

    def setupSpec() {
        OAuth2AuthenticationDetails authenticationDetails = Mock()
        authenticationDetails.getTokenValue() >> 'some.token.withCrypto'

        OAuth2Authentication authentication = Mock()
        authentication.getDetails() >> authenticationDetails

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
