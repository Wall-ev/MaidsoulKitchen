package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minersdelight;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.rule.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.rule.common.DefaultCookRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public final class MinersDelightRecipeSerializer {

    public static void registerRecipeInitializer() {
        if (Mods.MD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new MdCookingPotRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.MD.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecRuleSerializer<>(ModRecipeTypes.COOKING.get()));
        }
    }
}
