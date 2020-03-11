package com.bisnode.opa.spring.security.filter.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
@Import({OpaFilterConfiguration.class, OpaFilterConfigurer.class})
@ConditionalOnProperty(prefix = "opa.filter", name = "enabled", matchIfMissing = true)
@ConditionalOnClass(JwtAuthenticationToken.class)
class OpaSpringFilterAutoConfiguration {

}
