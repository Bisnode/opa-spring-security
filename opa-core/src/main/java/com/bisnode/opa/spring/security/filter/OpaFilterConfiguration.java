package com.bisnode.opa.spring.security.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OpaFilterConfiguration {
    @Value("opa.filter.policy")
    private String policy;
    @Value("opa.filter.instance")
    private String instance;

    public OpaFilterConfiguration() {
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
