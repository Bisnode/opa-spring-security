package com.bisnode.opa.spring.security.filter.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OpaFilterConfiguration.class, OpaFilterConfigurer.class})
@ConditionalOnProperty(prefix = "opa.filter", name = "enabled", matchIfMissing = true)
class OpaSpringFilterAutoConfiguration {

}
