package com.bisnode.opa.spring.security.filter.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "opa.filter")
class OpaFilterConfiguration {
    private boolean enabled;
    private String documentPath;
    private String instance;

    OpaFilterConfiguration() {
    }

    OpaFilterConfiguration(boolean enabled, String documentPath, String instance) {
        this.enabled = enabled;
        this.documentPath = documentPath;
        this.instance = instance;
    }

    public String getDocumentPath() {
        return this.documentPath;
    }

    public String getInstance() {
        return this.instance;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
