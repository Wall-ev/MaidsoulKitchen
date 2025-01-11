package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.farmersdelight;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.rule.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.rule.common.DefaultCookRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public final class FarmersDelightRecipeSerializer {

    public static void registerRecipeInitializer(){
        if (Mods.FD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new FdCookingPotRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.FD.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecRuleSerializer<>(ModRecipeTypes.COOKING.get()));
        }
    }

}
