package com.github.wallev.maidsoulkitchen.handler.serializer.kitchenkarrot;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.minersdelight.recipe.MdCookingPotRecipeSerializer;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class KitchenKarrotRecipeSerializer {

    public static void registerRecipeInitializer() {
        if (Mods.KK.isLoaded) {
            CookRecRecipeSerializerManager.registerCookRecSerializer(new MdCookingPotRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.KK.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(ModRecipeTypes.COOKING.get()));
        }
    }
}
