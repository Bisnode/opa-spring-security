package com.bisnode.opa.spring.security

import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import spock.lang.Specification

@SpringBootTest(
        classes = [SampleApplication],
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class OpaSpringFilterAutoConfigurationTest extends Specification {
    ApplicationContextRunner contextRunner

    def setup(){
        contextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(OpaSpringFilterAutoConfiguration.class))
    }
    def 'should start'(){
        expect:
          contextRunner
    }

    def 'Should create config from application-test.yml'() {
        OpaFilterConfiguration testConfig;
        given:

        when:
          testConfig = new OpaFilterConfiguration(true,"test-policy","test-instance")
        then:
          contextRunner.withSystemProperties("spring.profiles.active=test")
                  .run({ context ->
                      OpaFilterConfiguration filterConfiguration = context.getBean(OpaFilterConfiguration.class)
                      filterConfiguration.isEnabled() == testConfig.isEnabled()
                      filterConfiguration.getInstance() == testConfig.getInstance()
                      filterConfiguration.getPolicy() == testConfig.getPolicy()
                  })

    }
    def 'Should autoconfigure when no config is provided'(){
        OpaFilterConfiguration testConfig;
        given:

        when:
          testConfig = new OpaFilterConfiguration(true,null,null)
        then:
          contextRunner.withSystemProperties("spring.profiles.active=something")
                  .run({ context ->
                      OpaFilterConfiguration filterConfiguration = context.getBean(OpaFilterConfiguration.class)
                      filterConfiguration.isEnabled() == testConfig.isEnabled()
                      filterConfiguration.getInstance() == testConfig.getInstance()
                      filterConfiguration.getPolicy() == testConfig.getPolicy()
                  })


    }

}
