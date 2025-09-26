package com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.ITaskDataKey;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;
import java.util.function.Function;

public class BerryFruitData {
    public static final Codec<Map<String, Boolean>> MAP_CODEC = Codec.unboundedMap(Codec.STRING, Codec.BOOL).xmap(Maps::newHashMap, Function.identity());
    public static final Codec<BerryFruitData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MAP_CODEC.fieldOf("Rules").forGetter((berryFruitDataV2 -> berryFruitDataV2.rules)),
            Codec.INT.fieldOf("SearchYOffset").forGetter(berryFruitDataV2 -> berryFruitDataV2.searchYOffset)
    ).apply(instance, (BerryFruitData::new)));

    private Map<String, Boolean> rules;
    private int searchYOffset;

    public BerryFruitData(Map<String, Boolean> rules, int searchYOffset) {
        this.rules = rules;
        this.searchYOffset = searchYOffset;
    }

    public static BerryFruitData createDefaultBerry() {
        return new BerryFruitData(Maps.newHashMap(), 0);
    }

    public static BerryFruitData createDefaultBerry(Map<String, Boolean> rules) {
        return new BerryFruitData(rules, 0);
    }

    public static BerryFruitData createDefaultBerry(Map<String, Boolean> rules, int searchYOffset) {
        return new BerryFruitData(rules, searchYOffset);
    }

    public static BerryFruitData createDefaultFruit() {
        return new BerryFruitData(Maps.newHashMap(), 3);
    }

    public static BerryFruitData createDefaultFruit(Map<String, Boolean> rules) {
        return new BerryFruitData(rules, 3);
    }

    public static BerryFruitData createDefaultFruit(Map<String, Boolean> rules, int searchYOffset) {
        return new BerryFruitData(rules, searchYOffset);
    }

    public Map<String, Boolean> rules() {
        return rules;
    }

    public void setRules(Map<String, Boolean> rules) {
        this.rules = rules;
    }

    public void setRule(String rule, boolean value) {
        this.rules.put(rule, value);
    }

    public boolean containRule(String rule) {
        return this.rules.getOrDefault(rule, true);
    }

    public void increaseYOffset() {
        this.searchYOffset++;
    }

    public void decreaseYOffset() {
        this.searchYOffset--;
    }

    public int searchYOffset() {
        return searchYOffset;
    }

    public void setSearchYOffset(int searchYOffset) {
        this.searchYOffset = searchYOffset;
    }

    public static class Serializer implements ITaskDataKey.IVersionSerializer<BerryFruitData, BerryFruitData> {
        @Override
        public BerryFruitData toNew(BerryFruitData berryFruitData) {
            return berryFruitData;
        }

        @Override
        public Codec<BerryFruitData> getCodec() {
            return CODEC;
        }
    }
}
