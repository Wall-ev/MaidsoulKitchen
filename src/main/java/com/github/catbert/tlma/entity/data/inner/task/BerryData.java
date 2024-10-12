package com.github.catbert.tlma.entity.data.inner.task;

import com.github.catbert.tlma.task.farm.handler.v1.berry.BerryHandlerManager;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

public class BerryData {
    public static final Codec<List<String>> LIST_CODEC = Codec.STRING.listOf().xmap(Lists::newArrayList, Function.identity());
    public static final Codec<BerryData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LIST_CODEC.fieldOf("Rules").forGetter(BerryData::rules)
    ).apply(instance, BerryData::new));
    private List<String> rules;

    public BerryData() {
        this(Lists.newArrayList(BerryHandlerManager.MINECRAFT.getFarmHandler().getUid().toString(),
                BerryHandlerManager.COMPAT.getFarmHandler().getUid().toString()));
    }

    public BerryData(List<String> rules) {
        this.rules = rules;
    }

    public void setRules(List<String> rules) {
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
