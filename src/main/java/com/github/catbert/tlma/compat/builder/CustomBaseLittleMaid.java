package com.github.catbert.tlma.compat.builder;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import net.minecraft.resources.ResourceLocation;

public abstract class CustomBaseLittleMaid<T extends CustomBaseBuilder> implements ILittleMaid {
    public final ResourceLocation id;
    public CustomBaseLittleMaid(T builder) {
        this.id = builder.getId();
        builderDefaultInit(builder);
        builderCustomInit(builder);
    }

    public abstract void builderDefaultInit(T builder);
    public void builderCustomInit(T builder) {}
}
