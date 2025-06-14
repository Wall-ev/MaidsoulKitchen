package com.github.wallev.maidsoulkitchen.foundation.utility;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;


public class WrappedIngredient extends RecIngredient {
    private final ItemStack stack;

    protected WrappedIngredient(ItemStack stack) {
        super(Ingredient.of(stack));
        this.stack = stack;
    }

    @Override
    public int test(ItemStack itemStack) {
        return ingredient.test(itemStack) && itemStack.getCount() >= stack.getCount() ? stack.getCount() : 0;
    }
}
