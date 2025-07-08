package com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v0;


import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * use {@link BerryFruitData}
 */
@Deprecated(since = "0.2.0")
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

    protected static Map<String, Boolean> toMapRules(List<String> rules) {
        Map<String, Boolean> map = new HashMap<>();
        for (String rule : rules) {
            map.put(rule, true);
        }
        return map;
    }

    protected static Map<String, Boolean> toMapRules(FarmData farmData) {
        return toMapRules(farmData.rules);
    }
}
