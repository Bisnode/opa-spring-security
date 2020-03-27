package com.bisnode.opa.spring.security.filter.autoconfigure

import com.bisnode.opa.client.rest.ContentType
import com.github.tomakehurst.wiremock.WireMockServer
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.access.event.AuthorizationFailureEvent
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import spock.lang.Shared
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static com.bisnode.opa.client.rest.ContentType.Values.APPLICATION_JSON
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.any
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(
        classes = [SampleApplication, OpaFilterConfigurer, TestConfig],
        properties = "spring.main.allow-bean-definition-overriding=true",
        webEnvironment = RANDOM_PORT
)
class OpaFilterConfigurerSpec extends Specification {

    private static final String BASIC_AUTH_HEADER = "Basic ${Base64.encoder.encodeToString("someone:strongPass".getBytes())}"
    static final int OPA_PORT = 8181

    @LocalServerPort
    int applicationPort

    @Autowired
    private ApplicationEventPublisher applicationEventPublisherMock

    @Shared
    private WireMockServer wireMockServer = new WireMockServer(OPA_PORT)

    RESTClient restClient

    def setupSpec() {
        wireMockServer.start()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    def setup() {
        restClient = new RESTClient("http://localhost:$applicationPort", APPLICATION_JSON)
    }

    def 'should return 403 on OPA deny'() {
        given:
          wireMockServer.stubFor(any(anyUrl())
                  .willReturn(aResponse()
                          .withStatus(200)
                          .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                          .withBody('{"result": {"allow": false}}')
                  )
          )
        when:
          restClient.get(path: '/', headers: ["Authorization": BASIC_AUTH_HEADER])

        then:
          HttpResponseException e = thrown(HttpResponseException)
          e.response.status == 403
    }

    def 'should publish AuthorizationFailureEvent on OPA deny'() {
        given:
          wireMockServer.stubFor(any(anyUrl())
                  .willReturn(aResponse()
                          .withStatus(200)
                          .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                          .withBody('{"result": {"allow": false}}')
                  )
          )
        when:
          restClient.get(path: '/', headers: ["Authorization": BASIC_AUTH_HEADER])

        then:
          thrown(HttpResponseException)
          1 * applicationEventPublisherMock.publishEvent(_ as AuthorizationFailureEvent)
    }

    def 'should return 200 on OPA allow'() {
        given:
          wireMockServer.stubFor(any(anyUrl())
                  .willReturn(aResponse()
                          .withStatus(200)
                          .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                          .withBody('{"result": {"allow": true}}')
                  )
          )
        when:
          def response = restClient.get(path: '/', headers: ["Authorization": BASIC_AUTH_HEADER])

        then:
          noExceptionThrown()
          response.status == 200
    }

    def 'should return 403 on invalid result from OPA'() {
        given:
          wireMockServer.stubFor(any(anyUrl())
                  .willReturn(aResponse()
                          .withStatus(200)
                          .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                          .withBody('{"result": {}}')
                  )
          )
        when:
          restClient.get(path: '/', headers: ["Authorization": BASIC_AUTH_HEADER])

        then:
          HttpResponseException e = thrown(HttpResponseException)
          e.response.status == 403
    }

    def 'should not ask OPA for unauthenticated user'() {
        given:
          wireMockServer.stubFor(any(anyUrl())
                  .willReturn(aResponse()
                          .withStatus(200)
                          .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                          .withBody('{"result": {"allow": true}}')
                  )
          )
        when:
          restClient.get(path: '/')

        then:
          HttpResponseException e = thrown(HttpResponseException)
          e.response.status == 401
    }

    @TestConfiguration
    @EnableWebSecurity
    static class TestConfig extends WebSecurityConfigurerAdapter {

        @Bean
        OpaFilterConfiguration opaFilterConfiguration() {
            new OpaFilterConfiguration('some/policy', URI.create("http://localhost:$OPA_PORT"))
        }

        @Bean
        @Primary
        ApplicationEventPublisher applicationEventPublisher() {
            return new DetachedMockFactory().Mock(ApplicationEventPublisher)
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser(
                    User.builder()
                            .username("someone")
                            .password("{noop}strongPass")
                            .roles("USER")
                            .build()
            )
        }

    }

}
