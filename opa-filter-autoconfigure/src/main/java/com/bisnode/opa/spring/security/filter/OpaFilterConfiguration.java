package com.bisnode.opa.spring.security.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "opa.filter")
@ConditionalOnProperty(prefix = "opa.filter", name = "enabled", matchIfMissing = true)
public class OpaFilterConfiguration {
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaFilterConfiguration that = (OpaFilterConfiguration) o;
        return Objects.equals(policy, that.policy) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policy, instance);
    }

    public String toString() {
        return "OpaFilterConfiguration(policy=" + this.getPolicy() + ", instance=" + this.getInstance() + ")";
    }
}
