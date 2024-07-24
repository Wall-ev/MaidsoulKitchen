package com.github.catbert.tlma.compat.builder.bauble;

import com.github.catbert.tlma.compat.builder.CustomBaseBuilder;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class CustomBaubleBuilder extends CustomBaseBuilder<CustomBauble> {

    // CustomBaubleBuilder 便于KJS调试
    private static CustomBaubleBuilder customBaubleBuilder;

    // 对应Bauble的方法
    protected Item item;
    protected BiConsumer<EntityMaid, ItemStack> onTick;

    public CustomBaubleBuilder bindItem(Item item) {
        this.item = item;
        return this;
    }

    public CustomBaubleBuilder onTick(BiConsumer<EntityMaid, ItemStack> onTick) {
        this.onTick = onTick;
        return this;
    }

    public CustomBaubleBuilder setId(ResourceLocation id) {
        this.id = id;
        return this;
    }


    @Override
    protected CustomBauble getT() {
        return new CustomBauble(this);
    }

    @Override
    protected void init() {
        BaubleManager.init();
    }

    public static CustomBaubleBuilder getInstance() {
        if (customBaubleBuilder == null) {
            customBaubleBuilder = new CustomBaubleBuilder();
        }
        return customBaubleBuilder;
    }
}

