package com.bisnode.opa.spring.security.filter.autoconfigure

import com.bisnode.opa.client.rest.ContentType
import com.github.tomakehurst.wiremock.WireMockServer
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import spock.lang.Shared
import spock.lang.Specification

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
    static final int OPA_PORT = 8181

    @LocalServerPort
    int applicationPort

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
        restClient = new RESTClient("http://localhost:$applicationPort")
    }

    def 'should return 401 on opa deny'() {
        given:
            wireMockServer.stubFor(any(anyUrl())
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                            .withBody('{"result": {"allow": false}}')
                    )
            )
        when:
            restClient.get(path: '/', headers: [ContentType.HEADER_NAME: APPLICATION_JSON])

        then:
            HttpResponseException e = thrown(HttpResponseException)
            e.response.status == 403
    }

    def 'should return 200 on opa allow'() {
        given:
            wireMockServer.stubFor(any(anyUrl())
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader(ContentType.HEADER_NAME, APPLICATION_JSON)
                            .withBody('{"result": {"allow": true}}')
                    )
            )
        when:
            def response = restClient.get(path: '/', headers: [ContentType.HEADER_NAME: APPLICATION_JSON])


        then:
            noExceptionThrown()
            response.status == 200
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        OpaFilterConfiguration opaFilterConfiguration() {
            new OpaFilterConfiguration(true, 'some/policy', "http://localhost:$OPA_PORT")
        }
    }


}
