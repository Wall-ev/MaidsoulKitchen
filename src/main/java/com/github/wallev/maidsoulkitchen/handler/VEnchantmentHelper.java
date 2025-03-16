package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

@SuppressWarnings("all")
public class VEnchantmentHelper extends EnchantmentHelper {
    public static boolean hasSilkTouch(ItemStack pStack) {
        return getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pStack) > 0;
    }

}
