package com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer.ingredient.DbBrewingRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer.recipe.DbBrewingRecipeSerializer;

public class DrinkBeerRecipeInitializer {

    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager ) {
        if (Mods.DB.isLoaded) {
            cookRecRecipeInitializerManager.registerCookRecInitializer(new DbBrewingRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager) {
        if (Mods.DB.isLoaded) {
            cookRecIngredientSerializerManager.registerCookRecSerializer(new DbBrewingRecIngredientSerializer());
        }
    }

}
