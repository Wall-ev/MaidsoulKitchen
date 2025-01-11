package com.github.wallev.maidsoulkitchen.handler.serializer.minecraft.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.DefaultCookRecSerializer;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class SmeltingRecipeSerializer extends DefaultCookRecSerializer<AbstractCookingRecipe> {
    @SuppressWarnings("unchecked, rawtypes")
    public SmeltingRecipeSerializer() {
        super((RecipeType) RecipeType.SMELTING);
    }
}
