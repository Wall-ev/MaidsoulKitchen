package com.github.wallev.maidsoulkitchen.entity.data.inner.task;

import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry.BerryHandlerManager;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

public class BerryData extends FarmData{
    public static final Codec<List<String>> LIST_CODEC = Codec.STRING.listOf().xmap(Lists::newArrayList, Function.identity());
    public static final Codec<BerryData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LIST_CODEC.fieldOf("Rules").forGetter(BerryData::rules)
    ).apply(instance, BerryData::new));

    public BerryData() {
        this(Lists.newArrayList(BerryHandlerManager.MINECRAFT.getFarmHandler().getUid().toString(),
                BerryHandlerManager.COMPAT.getFarmHandler().getUid().toString()));
    }

    public BerryData(List<String> rules) {
        super(rules);
    }
}
