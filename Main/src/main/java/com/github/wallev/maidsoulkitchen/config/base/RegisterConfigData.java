package com.github.wallev.maidsoulkitchen.config.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RegisterConfigData {

    private final int version;
    private final Map<ResourceLocation, Config<?, ?>> regTaskConfig = new HashMap<>();
    private final Map<ResourceLocation, Config<?, ?>> cookTaskConfig = new HashMap<>();

    public RegisterConfigData(int version) {
        this.version = version;
    }

    public RegisterConfigData() {
        this(1);
    }

    public int getVersion() {
        return version;
    }

    public Map<ResourceLocation, Config<?, ?>> getRegTaskConfig() {
        return regTaskConfig;
    }

    public Map<ResourceLocation, Config<?, ?>> getCookTaskConfig() {
        return cookTaskConfig;
    }

    @SuppressWarnings("unchecked")
    public <T extends Config<?, ?>> T getRegTaskConfig(ResourceLocation key) {
        return (T) regTaskConfig.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends Config<?, ?>> T getCookTaskConfig(ResourceLocation key) {
        return (T) cookTaskConfig.get(key);
    }

    public void setRegTaskConfig(ResourceLocation key, Config<?, ?> config) {
        regTaskConfig.put(key, config);
    }

    public void setCookTaskConfig(ResourceLocation key, Config<?, ?> config) {
        cookTaskConfig.put(key, config);
    }

    @SuppressWarnings("unchecked")
    public <V, C extends Config<V, C>> void setRegTaskConfig(ResourceLocation key, V value) {
        ((Config<V, C>) regTaskConfig.get(key)).setValue(value);
    }

    @SuppressWarnings("unchecked")
    public <V, C extends Config<V, C>> void setCookTaskConfig(ResourceLocation key, V value) {
        ((Config<V, C>) cookTaskConfig.get(key)).setValue(value);
    }

    public static class IntegerConfig extends Config<Integer, IntegerConfig> {
        public static final Codec<IntegerConfig> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(Config::getKey),
                Codec.INT.fieldOf("value").forGetter(Config::getValue)
        ).apply(ins, IntegerConfig::new));

        public IntegerConfig(ResourceLocation key, Integer value) {
            super(key, value);
        }

        @Override
        public Codec<IntegerConfig> getCodec() {
            return CODEC;
        }

    }

    public static class FloatConfig extends Config<Float, FloatConfig> {
        public static final Codec<FloatConfig> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(Config::getKey),
                Codec.FLOAT.fieldOf("value").forGetter(Config::getValue)
        ).apply(ins, FloatConfig::new));

        public FloatConfig(ResourceLocation key, Float value) {
            super(key, value);
        }

        @Override
        public Codec<FloatConfig> getCodec() {
            return CODEC;
        }
    }

    public static class DoubleConfig extends Config<Double, DoubleConfig> {
        public static final Codec<DoubleConfig> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(Config::getKey),
                Codec.DOUBLE.fieldOf("value").forGetter(Config::getValue)
        ).apply(ins, DoubleConfig::new));

        public DoubleConfig(ResourceLocation key, Double value) {
            super(key, value);
        }

        @Override
        public Codec<DoubleConfig> getCodec() {
            return CODEC;
        }
    }

    public static class StringConfig extends Config<String, StringConfig> {
        public static final Codec<StringConfig> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(Config::getKey),
                Codec.STRING.fieldOf("value").forGetter(Config::getValue)
        ).apply(ins, StringConfig::new));

        public StringConfig(ResourceLocation key, String value) {
            super(key, value);
        }

        @Override
        public Codec<StringConfig> getCodec() {
            return CODEC;
        }
    }

    public static class BooleanConfig extends Config<Boolean, BooleanConfig> {

        public static final Codec<BooleanConfig> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(Config::getKey),
                Codec.BOOL.fieldOf("value").forGetter(Config::getValue)
        ).apply(ins, BooleanConfig::new));

        public BooleanConfig(ResourceLocation key, Boolean value) {
            super(key, value);
        }

        @Override
        public Codec<BooleanConfig> getCodec() {
            return CODEC;
        }
    }

    public static abstract class Config<T, C extends Config<T, C>> {
        private final ResourceLocation key;
        private T value;
        public Config(ResourceLocation key, T value) {
            this.key = key;
            this.value = value;
        }

        public ResourceLocation getKey() {
            return key;
        }

        public abstract Codec<C> getCodec();

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
