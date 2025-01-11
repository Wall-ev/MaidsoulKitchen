package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.brewinandchewin;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.rule.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.rule.common.DefaultCookRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

public final class BrewinandchewinRecipeSerializer {
    public static void registerRecipeInitializer(){
        if (Mods.BNCD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new BncKeyRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.BNCD.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecRuleSerializer<>(BCRecipeTypes.FERMENTING.get()));
        }
    }
}
