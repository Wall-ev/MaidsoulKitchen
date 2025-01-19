package com.github.wallev.maidsoulkitchen.handler.initializer.crockpot;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.crockpot.ingredient.CpCookingRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.crockpot.recipe.CrockPotCookingPotRecSerializer;

public class CrockPotRecipeInitializer {
    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager ) {
        if (Mods.CP.isLoaded) {
            cookRecRecipeInitializerManager.registerCookRecInitializer(new CrockPotCookingPotRecSerializer());
        }
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager) {
        if (Mods.CP.isLoaded) {
            cookRecIngredientSerializerManager.registerCookRecSerializer(new CpCookingRecIngredientSerializer());
        }
    }
}
