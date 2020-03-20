package com.bisnode.opa.spring.security.filter.autoconfigure;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.spring.security.filter.OpaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpaFilterConfigurer extends FilterRegistrationBean<OpaFilter> {

    @Autowired
    OpaFilterConfigurer(OpaFilterConfiguration opaFilterConfiguration) {
        OpaFilter opaFilter = new OpaFilter(newOpaClient(opaFilterConfiguration), opaFilterConfiguration.getDocumentPath());
        setFilter(opaFilter);
    }

    private OpaQueryApi newOpaClient(OpaFilterConfiguration opaFilterConfiguration) {
        return OpaClient.builder()
                .opaConfiguration(opaFilterConfiguration.getInstance())
                .build();
    }
}
