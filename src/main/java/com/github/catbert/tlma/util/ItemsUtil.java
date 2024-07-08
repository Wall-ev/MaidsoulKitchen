package com.github.catbert.tlma.util;

import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemsUtil {
    private ItemsUtil() {
    }

    /**
     * 获取物品Id
     */
    public static String getItemId(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        Preconditions.checkNotNull(key);
        return key.toString();
    }
}
