package com.bisnode.opa.spring.security.filter;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;

@Configuration
@Import(OpaFilterConfiguration.class)
@ConditionalOnProperty(prefix = "opa.filter", name = "enabled", matchIfMissing = true)
class OpaSpringFilterAutoConfiguration extends WebSecurityConfigurerAdapter {

    private OpaFilterConfiguration opaFilterConfiguration;

    @Autowired
    public OpaSpringFilterAutoConfiguration(OpaFilterConfiguration opaFilterConfiguration){
        this.opaFilterConfiguration = opaFilterConfiguration;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterAfter(newOpaFilter(), BearerTokenAuthenticationFilter.class);
    }


    private OpaFilter newOpaFilter() {
        return OpaFilter.builder()
                .opaQueryApi(newOpaClient())
                .policyName(opaFilterConfiguration.getPolicy())
                .build();
    }

    private OpaQueryApi newOpaClient() {
        return OpaClient.builder()
                .opaConfiguration(opaFilterConfiguration.getInstance())
                .build();
    }

    public OpaFilterConfiguration getOpaFilterConfiguration() {
        return opaFilterConfiguration;
    }

}
