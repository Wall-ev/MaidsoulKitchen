package com.github.wallev.maidsoulkitchen.mixinmanager.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;

public class TaskMapConfig {
    public static final Codec<Map<String, TaskConfigConfig>> MAP_CODEC = Codec.unboundedMap(Codec.STRING, TaskConfigConfig.CODEC).xmap((s) -> {
        return new HashMap<>(s);
    }, (map0) -> {
        return map0;
    });

    public static final Codec<TaskMapConfig> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.unboundedMap(Codec.STRING, TaskConfigConfig.CODEC).fieldOf("task").forGetter(o -> o.map)
    ).apply(ins, TaskMapConfig::new));
    private Map<String, TaskConfigConfig> map;

    public TaskMapConfig(Map<String, TaskConfigConfig> map) {
        this.map = map;
    }

    public Map<String, TaskConfigConfig> getMap() {
        return map;
    }

    public void setMap(Map<String, TaskConfigConfig> map) {
        this.map = map;
    }
}
