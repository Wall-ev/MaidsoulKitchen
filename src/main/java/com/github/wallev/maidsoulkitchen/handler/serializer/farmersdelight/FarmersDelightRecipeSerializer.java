package com.github.wallev.maidsoulkitchen.handler.serializer.farmersdelight;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.farmersdelight.recipe.FdCookingPotRecipeSerializer;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class FarmersDelightRecipeSerializer {

    public static void registerRecipeInitializer(){
        if (Mods.FD.isLoaded) {
            CookRecRecipeSerializerManager.registerCookRecSerializer(new FdCookingPotRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.FD.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(ModRecipeTypes.COOKING.get()));
        }
    }

}
