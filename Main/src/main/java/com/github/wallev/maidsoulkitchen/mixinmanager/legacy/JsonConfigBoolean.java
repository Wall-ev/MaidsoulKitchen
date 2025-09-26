package com.github.wallev.maidsoulkitchen.mixinmanager.legacy;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import org.jetbrains.annotations.Nullable;

public class JsonConfigBoolean implements IJsonConfig<Boolean> {
    private final String key;
    private final Boolean defaultValue;
    private Boolean value;
    @Nullable
    private Mods mod;

    public JsonConfigBoolean(String key, Boolean defaultValue, Mods mod) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = defaultValue;

        this.mod = mod;
    }

    public JsonConfigBoolean(Boolean value, Boolean defaultValue, String key) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.key = key;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getDefault() {
        return this.defaultValue;
    }

    @Override
    public Boolean get() {
        return this.value;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Nullable
    public Mods getMod() {
        return mod;
    }

    public boolean canLoaded() {
        if (this.mod != null) {
            return mod.versionLoad() && value;
        } else {
            return value;
        }
    }
}
