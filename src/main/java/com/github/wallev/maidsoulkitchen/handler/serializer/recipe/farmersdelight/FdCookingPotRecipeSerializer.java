package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.farmersdelight;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.ContainerCookRecSerializer;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class FdCookingPotRecipeSerializer extends ContainerCookRecSerializer<CookingPotRecipe> {
    public FdCookingPotRecipeSerializer() {
        super(ModRecipeTypes.COOKING.get());
    }

    @Override
    public ItemStack getContainer(CookingPotRecipe recipe) {
        return recipe.getOutputContainer();
    }
}
