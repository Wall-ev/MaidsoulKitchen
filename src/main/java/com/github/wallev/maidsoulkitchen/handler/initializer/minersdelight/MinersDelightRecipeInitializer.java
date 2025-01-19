package com.github.wallev.maidsoulkitchen.handler.initializer.minersdelight;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.minersdelight.recipe.MdCookingPotRecipeSerializer;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class MinersDelightRecipeInitializer {

    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager ) {
        if (Mods.MD.isLoaded) {
            cookRecRecipeInitializerManager.registerCookRecInitializer(new MdCookingPotRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager) {
        if (Mods.MD.isLoaded) {
            cookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(ModRecipeTypes.COOKING.get()));
        }
    }
}
