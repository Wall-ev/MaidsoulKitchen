package com.github.wallev.maidsoulkitchen.handler.initializer.minecraft.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.DefaultCookRecSerializer;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class BlastingRecipeSerializer extends DefaultCookRecSerializer<BlastingRecipe> {
    public BlastingRecipeSerializer() {
        super(RecipeType.BLASTING);
    }
}
