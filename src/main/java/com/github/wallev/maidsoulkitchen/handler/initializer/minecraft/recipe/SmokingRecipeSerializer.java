package com.github.wallev.maidsoulkitchen.handler.initializer.minecraft.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.DefaultCookRecSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;

public class SmokingRecipeSerializer extends DefaultCookRecSerializer<SmokingRecipe> {
    public SmokingRecipeSerializer() {
        super(RecipeType.SMOKING);
    }
}
