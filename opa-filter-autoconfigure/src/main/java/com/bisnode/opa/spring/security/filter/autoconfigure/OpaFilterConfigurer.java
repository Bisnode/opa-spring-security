package com.bisnode.opa.spring.security.filter.autoconfigure;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.spring.security.filter.OpaAccesDecisionVoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;

@Configuration
class OpaFilterConfigurer extends WebSecurityConfigurerAdapter {

    private final OpaFilterConfiguration opaFilterConfiguration;

    @Autowired
    OpaFilterConfigurer(OpaFilterConfiguration opaFilterConfiguration) {
        this.opaFilterConfiguration = opaFilterConfiguration;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {

        List<AccessDecisionVoter<?>> voters = List.of(
                new OpaAccesDecisionVoter(newOpaClient(), opaFilterConfiguration.getPolicy()),
                new RoleVoter(),
                new AuthenticatedVoter());
        return new UnanimousBased(voters);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        OpaFilter opaFilter = new OpaFilter(newOpaClient(), opaFilterConfiguration.getPolicy());
//        http.addFilterAfter(opaFilter, BearerTokenAuthenticationFilter.class);

        http.authorizeRequests().anyRequest().authenticated()
                .accessDecisionManager(accessDecisionManager());
    }

    private OpaQueryApi newOpaClient() {
        return OpaClient.builder()
                .opaConfiguration(opaFilterConfiguration.getInstance())
                .build();
    }
}
