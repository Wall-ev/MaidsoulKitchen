package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.DefaultCookRecSerializer;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class SmeltingRecipeSerializer extends DefaultCookRecSerializer<AbstractCookingRecipe> {
    @SuppressWarnings("unchecked, rawtypes")
    public SmeltingRecipeSerializer() {
        super((RecipeType) RecipeType.SMELTING);
    }
}
