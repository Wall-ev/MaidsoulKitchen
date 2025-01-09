package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.DefaultCookRecSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;

public class SmokingRecipeSerializer extends DefaultCookRecSerializer<SmokingRecipe> {
    public SmokingRecipeSerializer() {
        super(RecipeType.SMOKING);
    }
}
