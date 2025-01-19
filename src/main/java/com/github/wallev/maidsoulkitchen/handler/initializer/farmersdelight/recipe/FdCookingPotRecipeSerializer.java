package com.github.wallev.maidsoulkitchen.handler.initializer.farmersdelight.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.OutputContainerCookRecSerializer;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class FdCookingPotRecipeSerializer extends OutputContainerCookRecSerializer<CookingPotRecipe> {
    public FdCookingPotRecipeSerializer() {
        super(ModRecipeTypes.COOKING.get());
    }

    @Override
    public ItemStack getContainer(CookingPotRecipe recipe) {
        return recipe.getOutputContainer();
    }
}
