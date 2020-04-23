package com.bisnode.opa.spring.security.filter.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "opa.filter")
class OpaFilterConfiguration {
    private String documentPath;
    private List<String> endpointsWhitelist = new ArrayList<>();
    private URI instance = URI.create("http://localhost:8181");

    OpaFilterConfiguration() {
    }

    OpaFilterConfiguration(String documentPath, List<String> endpointsWhitelist, URI instance) {
        this.documentPath = documentPath;
        this.endpointsWhitelist = endpointsWhitelist;
        this.instance = instance;
    }

    public String getDocumentPath() {
        return this.documentPath;
    }

    public List<String> getEndpointsWhitelist() {
        return endpointsWhitelist;
    }

    public URI getInstance() {
        return this.instance;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public void setEndpointsWhitelist(List<String> endpointsWhitelist) {
        this.endpointsWhitelist = endpointsWhitelist;
    }

    public void setInstance(URI instance) {
        this.instance = instance;
    }

}
