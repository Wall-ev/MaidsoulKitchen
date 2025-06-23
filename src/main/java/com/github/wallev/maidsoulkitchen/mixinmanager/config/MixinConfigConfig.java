package com.github.wallev.maidsoulkitchen.mixinmanager.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;

public class MixinConfigConfig {
    public static final Codec<Map<String, Boolean>> MAP_CODEC = Codec.unboundedMap(Codec.STRING, Codec.BOOL).xmap((s) -> {
        return new HashMap<>(s);
    }, (map0) -> {
        return map0;
    });
    public static final Codec<MixinConfigConfig> CODEC = RecordCodecBuilder.create(instance -> {
        RecordCodecBuilder<MixinConfigConfig, Map<String, Boolean>> mixinsCodec = MAP_CODEC.fieldOf("mixins").forGetter((instance0) -> {
            return instance0.getMixins();
        });

        return instance.group(mixinsCodec).apply(instance, (map) -> {
            return new MixinConfigConfig(map);
        });
    });

    private Map<String, Boolean> mixins;

    public MixinConfigConfig(Map<String, Boolean> mixins) {
        this.mixins = mixins;
    }

    public Map<String, Boolean> getMixins() {
        return mixins;
    }

    public void setMixins(Map<String, Boolean> mixins) {
        this.mixins = mixins;
    }
}
