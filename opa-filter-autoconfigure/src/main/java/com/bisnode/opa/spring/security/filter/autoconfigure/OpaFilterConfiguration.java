package com.bisnode.opa.spring.security.filter.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "opa.filter")
class OpaFilterConfiguration {
    private String documentPath;
    private String whitelist;
    private URI instance = URI.create("http://localhost:8181");

    OpaFilterConfiguration() {
    }

    OpaFilterConfiguration(String documentPath, String whitelist, URI instance) {
        this.documentPath = documentPath;
        this.whitelist = whitelist;
        this.instance = instance;
    }

    public String getDocumentPath() {
        return this.documentPath;
    }

    public String getWhitelist() {
        return whitelist;
    }

    public URI getInstance() {
        return this.instance;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }

    public void setInstance(URI instance) {
        this.instance = instance;
    }

}
