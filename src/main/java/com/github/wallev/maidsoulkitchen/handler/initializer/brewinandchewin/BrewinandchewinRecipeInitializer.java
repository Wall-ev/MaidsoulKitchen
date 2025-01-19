package com.github.wallev.maidsoulkitchen.handler.initializer.brewinandchewin;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.brewinandchewin.recipe.BncKeyRecipeSerializer;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

public class BrewinandchewinRecipeInitializer {
    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager ){
        if (Mods.BNCD.isLoaded) {
            cookRecRecipeInitializerManager.registerCookRecInitializer(new BncKeyRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager){
        if (Mods.BNCD.isLoaded) {
            cookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(BCRecipeTypes.FERMENTING.get()));
        }
    }
}
