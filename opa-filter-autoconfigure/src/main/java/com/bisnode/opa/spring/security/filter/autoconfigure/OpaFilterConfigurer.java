package com.bisnode.opa.spring.security.filter.autoconfigure;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.spring.security.filter.OpaFilter;
import com.bisnode.opa.spring.security.filter.decision.AccessDecider;
import com.bisnode.opa.spring.security.filter.decision.request.ByRequestDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Configuration
class OpaFilterConfigurer extends FilterRegistrationBean<OpaFilter> {

    @Autowired
    OpaFilterConfigurer(OpaFilterConfiguration configuration) {
        AccessDecider<HttpServletRequest> decider = ByRequestDecider.withDefaultSupplier(
                newOpaClient(configuration.getInstance()),
                configuration.getDocumentPath()
        );

        setFilter(new OpaFilter(decider));
    }

    private OpaQueryApi newOpaClient(URI instance) {
        return OpaClient.builder()
                .opaConfiguration(instance.toString())
                .build();
    }
}
