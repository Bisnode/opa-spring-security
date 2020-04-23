package com.bisnode.opa.spring.security.filter.autoconfigure;

import com.bisnode.opa.client.OpaClient;
import com.bisnode.opa.client.query.OpaQueryApi;
import com.bisnode.opa.spring.security.filter.OpaFilter;
import com.bisnode.opa.spring.security.filter.decision.AccessDecider;
import com.bisnode.opa.spring.security.filter.decision.request.ByRequestDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Configuration
class OpaFilterConfigurer extends FilterRegistrationBean<OpaFilter> {

    @Autowired
    OpaFilterConfigurer(OpaFilterConfiguration configuration, ApplicationEventPublisher eventPublisher) {
        AccessDecider<HttpServletRequest> decider = ByRequestDecider.withDefaultSupplier(
                newOpaClient(configuration.getInstance()),
                configuration.getDocumentPath()
        );
        RequestMatcher whitelistRequestMatcher = whitelistRequestMatcher(configuration.getEndpointsWhitelist());

        setFilter(new OpaFilter(decider, eventPublisher, whitelistRequestMatcher));
    }

    private OpaQueryApi newOpaClient(URI instance) {
        return OpaClient.builder()
                .opaConfiguration(instance.toString())
                .build();
    }

    private RequestMatcher whitelistRequestMatcher(List<String> whitelistPatterns) {
        List<RequestMatcher> matchers = Optional.ofNullable(whitelistPatterns)
                .orElse(List.of()).stream()
                .map(String::trim)
                .filter(not(String::isEmpty))
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        return matchers.isEmpty() ? request -> false : new OrRequestMatcher(matchers);
    }
}
