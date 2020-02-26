package com.bisnode.opa.spring.security;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.spring.security.filter.OpaFilter;
import com.bisnode.opa.spring.security.filter.OpaFilterConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@Import(OpaFilterConfiguration.class)
@ConditionalOnProperty(prefix = "opa.filter.auto-config", name = "enabled", matchIfMissing = true)
public class OpaSpringFilterAutoConfiguration extends OpaFilterConfigurer {


    private OpaFilterConfiguration opaFilterConfiguration;

    @Autowired
    public OpaSpringFilterAutoConfiguration(OpaFilterConfiguration opaFilterConfiguration){
        this.opaFilterConfiguration = opaFilterConfiguration;
    }

    @Override
    public void configure(HttpSecurity http) {
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

    public OpaFilterConfiguration getOpaFilterConfiguration() {
        return opaFilterConfiguration;
    }

}
