package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.drinkbeer;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.DefaultCookRecSerializer;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;

public class DbBrewingRecipeSerializer extends DefaultCookRecSerializer<BrewingRecipe> {
    public DbBrewingRecipeSerializer() {
        super(RecipeRegistry.RECIPE_TYPE_BREWING.get());
    }
}
