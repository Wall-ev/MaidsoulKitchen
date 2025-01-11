package com.github.wallev.maidsoulkitchen.handler.serializer.kitchenkarrot.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.DefaultCookRecSerializer;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class KkAirCompressorRecipeSerializer extends DefaultCookRecSerializer<AirCompressorRecipe> {
    public KkAirCompressorRecipeSerializer() {
        super(RecipeTypes.AIR_COMPRESSOR.get());
    }

    @Override
    protected List<Ingredient> getIngredients(AirCompressorRecipe recipe) {
        return super.getIngredients(recipe);
    }
}
