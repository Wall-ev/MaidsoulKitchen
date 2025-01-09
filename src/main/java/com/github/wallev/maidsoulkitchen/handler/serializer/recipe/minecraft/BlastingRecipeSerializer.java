package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.DefaultCookRecSerializer;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class BlastingRecipeSerializer extends DefaultCookRecSerializer<BlastingRecipe> {
    public BlastingRecipeSerializer() {
        super(RecipeType.BLASTING);
    }
}
