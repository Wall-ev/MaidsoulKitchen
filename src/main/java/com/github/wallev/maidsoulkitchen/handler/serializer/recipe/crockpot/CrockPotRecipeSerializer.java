package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.crockpot;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.rule.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.rule.crockpot.CpCookingRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;

public final class CrockPotRecipeSerializer {
    public static void registerRecipeInitializer(){
        if (Mods.CP.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new CrockPotCookingPotRecSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.CP.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new CpCookingRecRuleSerializer());
        }
    }
}
