package com.github.wallev.maidsoulkitchen.handler.serializer.brewinandchewin;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.brewinandchewin.recipe.BncKeyRecipeSerializer;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

public class BrewinandchewinRecipeSerializer {
    public static void registerRecipeInitializer(){
        if (Mods.BNCD.isLoaded) {
            CookRecRecipeSerializerManager.registerCookRecSerializer(new BncKeyRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.BNCD.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(BCRecipeTypes.FERMENTING.get()));
        }
    }
}
