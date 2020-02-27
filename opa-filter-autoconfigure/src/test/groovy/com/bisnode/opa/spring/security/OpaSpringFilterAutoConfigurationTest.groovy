package com.bisnode.opa.spring.security

import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest(
		classes = [SampleApplication],
		webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@ActiveProfiles(value = "test")
class OpaSpringFilterAutoConfigurationTest extends Specification {
	ApplicationContextRunner contextRunner

	def setup(){
		contextRunner = new ApplicationContextRunner()

	}

	def 'should start'() {
		expect:
			contextRunner
	}

	@Unroll
	def 'Should create config where enabled is #enabled and policy is #policy and instance is #instance and springProfile is #springProfile'() {
		OpaFilterConfiguration testConfig
		given:
		when:
			contextRunner.withSystemProperties("spring.profiles.active=" + springProfile)
					.withConfiguration(AutoConfigurations.of(OpaSpringFilterAutoConfiguration.class))
			.withConfiguration()
					.run({ context ->
						testConfig = context.getBean(OpaFilterConfiguration.class)
						context.getEnvironment().getActiveProfiles()[0] == springProfile
						println(context.getEnvironment())
					})
		then:
			testConfig.isEnabled() == enabled
			testConfig.getInstance() == instance
			testConfig.getPolicy() == policy

		where:
			enabled | policy           | instance           | springProfile
			true    | null             | null               | "prod"
			false   | null             | null               | "prod"
			true    | "some-policy"    | "some-instance"    | "prod"
			true    | "some-policy"    | "some-instance"    | "test"
			true    | "default-policy" | "default-instance" | "default"
			false   | "default-policy" | "default-instance" | "default"


	}


	def 'Should autoconfigure when no config is provided'() {
		OpaFilterConfiguration testConfig;
		given:
			contextRunner.withConfiguration(AutoConfigurations.of(OpaSpringFilterAutoConfiguration.class))

		when:
			testConfig = new OpaFilterConfiguration(true, null, null)
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
