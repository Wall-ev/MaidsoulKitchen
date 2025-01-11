package com.github.wallev.maidsoulkitchen.handler.serializer.drinkbeer;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.drinkbeer.ingredient.DbBrewingRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.drinkbeer.recipe.DbBrewingRecipeSerializer;

public class DrinkBeerRecipeSerializer {

    public static void registerRecipeInitializer() {
        if (Mods.DB.isLoaded) {
            CookRecRecipeSerializerManager.registerCookRecSerializer(new DbBrewingRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer() {
        if (Mods.DB.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DbBrewingRecIngredientSerializer());
        }
    }

}
