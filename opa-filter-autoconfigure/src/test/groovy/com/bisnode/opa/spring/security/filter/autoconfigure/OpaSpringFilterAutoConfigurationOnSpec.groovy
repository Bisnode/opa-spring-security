package com.bisnode.opa.spring.security.filter.autoconfigure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK

@SpringBootTest(
        classes = [SampleApplication],
        properties = [
                "opa.filter.documentPath=some/policy",
                "opa.filter.instance=http://someUrl:1234/",
                "opa.filter.endpointsWhitelist=/some/whitelisted/endpoint/**"
        ],
        webEnvironment = MOCK
)
class OpaSpringFilterAutoConfigurationOnSpec extends Specification implements BeanVerifier {

    @Autowired
    ApplicationContext context

    @Autowired
    OpaFilterConfiguration opaFilterConfiguration

    def 'should autowire context'() {
        expect:
            context
    }

    @Unroll
    def 'should autowire #beanClass.simpleName'() {
        expect:
            possibleBeanNamesOf(beanClass).any { beanName -> context.containsBean(beanName) }
        where:
            beanClass << [OpaSpringFilterAutoConfiguration, OpaFilterConfigurer, OpaFilterConfiguration]
    }

    def 'should read properties'() {
        expect:
            opaFilterConfiguration.instance == URI.create('http://someUrl:1234/')
            opaFilterConfiguration.documentPath == 'some/policy'
            opaFilterConfiguration.endpointsWhitelist.contains('/some/whitelisted/endpoint/**')
            opaFilterConfiguration.endpointsWhitelist.size() == 1
    }

}
