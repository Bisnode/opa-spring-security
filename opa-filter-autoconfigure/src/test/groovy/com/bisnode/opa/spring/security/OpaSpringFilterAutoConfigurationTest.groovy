package com.bisnode.opa.spring.security


import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
class OpaSpringFilterAutoConfigurationTest extends Specification {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(OpaSpringFilterAutoConfiguration.class));

//    @Autowired
//    private OpaSpringFilterAutoConfiguration opaSpringFilterAutoConfiguration
//    @Autowired
//    private OpaFilterConfiguration opaFilterConfiguration
//
//    def 'Should create config with injected mock'() {
//        given:
//        when:
//         true
//        then:
//          opaFilterConfiguration.getInstance()=="test-instance"
//          opaSpringFilterAutoConfiguration.getOpaFilterConfiguration().getInstance() == "test-policy"
//          opaSpringFilterAutoConfiguration.getOpaFilterConfiguration().getInstance() == "test-instance"
//
//
//    }
}
