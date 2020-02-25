package com.bisnode.opa.spring.security.filter;

import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

public abstract class OpaFilterConfigurer extends ResourceServerConfigurerAdapter {
    OpaFilterConfiguration opaFilterConfiguration;

}
