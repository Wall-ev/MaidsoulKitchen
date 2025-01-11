package com.github.wallev.maidsoulkitchen.handler.serializer.drinkbeer.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.DefaultCookRecSerializer;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;

public class DbBrewingRecipeSerializer extends DefaultCookRecSerializer<BrewingRecipe> {
    public DbBrewingRecipeSerializer() {
        super(RecipeRegistry.RECIPE_TYPE_BREWING.get());
    }
}
