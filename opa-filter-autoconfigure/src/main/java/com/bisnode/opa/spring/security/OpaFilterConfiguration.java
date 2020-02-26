package com.bisnode.opa.spring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class OpaFilterConfiguration {
    private boolean enabled;
    private String policy;
    private String instance;

    public OpaFilterConfiguration(@Value("opa.filter.auto-config.enabled") boolean enabled,
                                  @Value("opa.filter.policy") String policy,
                                  @Value("opa.filter.instance") String instance) {
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
