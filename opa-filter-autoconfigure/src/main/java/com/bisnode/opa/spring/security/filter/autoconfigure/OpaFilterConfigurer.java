package com.bisnode.opa.spring.security.filter.autoconfigure;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.spring.security.filter.OpaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;

@Configuration
class OpaFilterConfigurer extends WebSecurityConfigurerAdapter {

    private final OpaFilterConfiguration opaFilterConfiguration;

    @Autowired
    OpaFilterConfigurer(OpaFilterConfiguration opaFilterConfiguration) {
        this.opaFilterConfiguration = opaFilterConfiguration;
    }

    @Override
    public void configure(HttpSecurity http) {
        OpaFilter opaFilter = new OpaFilter(newOpaClient(), opaFilterConfiguration.getPolicy());
        http.addFilterAfter(opaFilter, BearerTokenAuthenticationFilter.class);
    }

    private OpaQueryApi newOpaClient() {
        return OpaClient.builder()
                .opaConfiguration(opaFilterConfiguration.getInstance())
                .build();
    }
}
