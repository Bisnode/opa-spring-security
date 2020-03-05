package com.bisnode.opa.spring.security.filter.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "opa.filter")
class OpaFilterConfiguration {
    private boolean enabled;
    private String policy;
    private String instance;

    public OpaFilterConfiguration() {
    }

    public OpaFilterConfiguration(boolean enabled, String policy, String instance) {
        this.enabled = enabled;
        this.policy = policy;
        this.instance = instance;
    }

    public String getPolicy() {
        return this.policy;
    }

    public String getInstance() {
        return this.instance;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
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
