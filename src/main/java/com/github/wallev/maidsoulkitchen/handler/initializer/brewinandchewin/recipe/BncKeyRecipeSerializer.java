package com.github.wallev.maidsoulkitchen.handler.initializer.brewinandchewin.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.OutputContainerCookRecSerializer;
import net.minecraft.world.item.ItemStack;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

public class BncKeyRecipeSerializer extends OutputContainerCookRecSerializer<KegRecipe> {
    public BncKeyRecipeSerializer() {
        super(BCRecipeTypes.FERMENTING.get());
    }

    @Override
    public ItemStack getContainer(KegRecipe recipe) {
        return recipe.getOutputContainer();
    }
}
