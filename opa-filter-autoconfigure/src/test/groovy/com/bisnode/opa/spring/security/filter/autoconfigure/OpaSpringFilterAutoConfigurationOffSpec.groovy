package com.bisnode.opa.spring.security.filter.autoconfigure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK

@SpringBootTest(
        classes = [SampleApplication],
        properties = ["opa.filter.enabled=false"],
        webEnvironment = MOCK
)
class OpaSpringFilterAutoConfigurationOffSpec extends Specification implements BeanVerifier {

    @Autowired
    ApplicationContext context

    def 'should autowire context'() {
        expect:
            context
    }

    @Unroll
    def 'should not autowire #beanClass.simpleName'() {
        expect:
            possibleBeanNamesOf(beanClass).each { beanName -> !context.containsBean(beanName) }
        where:
            beanClass << [OpaSpringFilterAutoConfiguration, OpaFilterConfigurer, OpaFilterConfiguration]
    }

}
