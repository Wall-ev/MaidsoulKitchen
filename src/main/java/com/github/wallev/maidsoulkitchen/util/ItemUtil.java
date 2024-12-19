package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemUtil {
    private ItemUtil() {
    }

    /**
     * 获取物品Id
     */
    public static String getId(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key != null) {
            return key.toString();
        }
        return "";
    }

    public static String getId(ItemStack stack) {
        return getId(stack.getItem());
    }
}
