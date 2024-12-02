package com.github.wallev.farmsoulkitchen.entity.data.inner.task;

import java.util.List;

public abstract class FarmData {

    protected List<String> rules;

    public FarmData(List<String> rules) {
        this.rules = rules;
    }

    protected void setRules(List<String> rules) {
        this.rules = rules;
    }

    public List<String> rules() {
        return rules;
    }

    public void addRule(String rule) {
        this.rules.add(rule);
    }

    public void removeRule(String rule) {
        this.rules.remove(rule);
    }

    public void addOrRemoveRule(String rule) {
        if (this.rules.contains(rule)) {
            this.rules.remove(rule);
        } else {
            this.rules.add(rule);
        }
    }
}
