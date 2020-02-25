package com.bisnode.opa.spring.security;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.spring.security.filter.OpaFilter;
import com.bisnode.opa.spring.security.filter.OpaFilterConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
public class OpaSpringFilterConfigurer extends com.bisnode.opa.spring.security.filter.OpaFilterConfigurer {
    private OpaFilterConfiguration opaFilterConfiguration;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(newOpaFilter(), AbstractPreAuthenticatedProcessingFilter.class);
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


}
