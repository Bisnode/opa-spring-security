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
                "opa.filter.policy=some/policy",
                "opa.filter.instance=http://someUrl:1234/"
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
            opaFilterConfiguration.instance == 'http://someUrl:1234/'
            opaFilterConfiguration.policy == 'some/policy'
    }

}
