package com.bisnode.opa.spring.security.filter.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "opa.filter")
class OpaFilterConfiguration {
    private String documentPath;
    private URI instance = URI.create("http://localhost:8080");

    OpaFilterConfiguration() {
    }

    OpaFilterConfiguration(String documentPath, URI instance) {
        this.documentPath = documentPath;
        this.instance = instance;
    }

    public String getDocumentPath() {
        return this.documentPath;
    }

    public URI getInstance() {
        return this.instance;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public void setInstance(URI instance) {
        this.instance = instance;
    }

}
