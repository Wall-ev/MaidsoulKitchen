package com.github.wallev.farmsoulkitchen.entity.data.inner.task;

import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.fruit.FruitHandlerManager;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

public class FruitData extends FarmData{
    public static final Codec<List<String>> LIST_CODEC = Codec.STRING.listOf().xmap(Lists::newArrayList, Function.identity());
    public static final Codec<FruitData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("SearchYOffset").forGetter(FruitData::searchYOffset),
            LIST_CODEC.fieldOf("Rules").forGetter(FruitData::rules)
    ).apply(instance, FruitData::new));
    private int searchYOffset;

    public FruitData() {
        this(3, Lists.newArrayList(FruitHandlerManager.COMPAT.getFarmHandler().getUid().toString()));
    }

    public FruitData(int searchYOffset, List<String> rules) {
        super(rules);
        this.searchYOffset = searchYOffset;
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
}
