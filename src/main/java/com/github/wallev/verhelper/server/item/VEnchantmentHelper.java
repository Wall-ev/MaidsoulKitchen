package com.github.wallev.verhelper.server.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class VEnchantmentHelper extends EnchantmentHelper {
    public static boolean hasSilkTouch(ItemStack itemStack) {
        return getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) > 0;
    }
}
