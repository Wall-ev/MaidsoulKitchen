package com.github.catbert.tlma.entity.data.inner.task;

import com.github.catbert.tlma.task.farm.handler.v1.fruit.FruitHandlerManager;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

public class FruitData {
    public static final Codec<List<String>> LIST_CODEC = Codec.STRING.listOf().xmap(Lists::newArrayList, Function.identity());
    public static final Codec<FruitData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("SearchYOffset").forGetter(FruitData::searchYOffset),
            LIST_CODEC.fieldOf("Rules").forGetter(FruitData::rules)
    ).apply(instance, FruitData::new));
    private int searchYOffset;
    private List<String> rules;

    public FruitData() {
        this(3, Lists.newArrayList(FruitHandlerManager.COMPAT.getFarmHandler().getUid().toString()));
    }

    public FruitData(int searchYOffset, List<String> rules) {
        this.searchYOffset = searchYOffset;
        this.rules = rules;
    }

    public int searchYOffset() {
        return searchYOffset;
    }

    public void setSearchYOffset(int searchYOffset) {
        this.searchYOffset = searchYOffset;
    }

    public void increaseYOffset() {
        this.searchYOffset++;
    }

    public void decreaseYOffset() {
        this.searchYOffset--;
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
