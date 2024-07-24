package com.github.catbert.tlma.compat.builder;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public abstract class CustomBaseBuilder<T extends CustomBaseLittleMaid>{
    // CustomBaubleBuilder 便于KJS调试
    private T t;
    // 用于唯一标识符,便于像饰品的事件监听
    protected ResourceLocation id;

    public ResourceLocation getId() {
        return id;
    }

    public T build() {
        return build(false);
    }

    public T build(boolean init) {
        TouhouLittleMaid.EXTENSIONS.add(getT());
        if (init) init();
        return getT();
    }

    public T debugBuild(boolean init) {
        if (t == null) {
            t = getT();
        } else {
            t.builderDefaultInit(this);
        }

        if (!TouhouLittleMaid.EXTENSIONS.contains(t)) {
            TouhouLittleMaid.EXTENSIONS.add(t);
        }

        if (init) init();
        return t;
    }

    @Nullable
    protected abstract T getT();
    protected abstract void init();
}
