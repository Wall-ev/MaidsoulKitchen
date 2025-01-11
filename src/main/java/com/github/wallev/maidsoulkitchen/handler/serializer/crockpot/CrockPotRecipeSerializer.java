package com.github.wallev.maidsoulkitchen.handler.serializer.crockpot;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.crockpot.ingredient.CpCookingRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.crockpot.recipe.CrockPotCookingPotRecSerializer;

public class CrockPotRecipeSerializer {
    public static void registerRecipeInitializer() {
        if (Mods.CP.isLoaded) {
            CookRecRecipeSerializerManager.registerCookRecSerializer(new CrockPotCookingPotRecSerializer());
        }
    }

    public static void registerIngredientSerializer() {
        if (Mods.CP.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new CpCookingRecIngredientSerializer());
        }
    }
}
