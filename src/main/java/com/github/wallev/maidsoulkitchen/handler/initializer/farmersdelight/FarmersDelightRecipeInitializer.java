package com.github.wallev.maidsoulkitchen.handler.initializer.farmersdelight;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.base.rule.MaidCookBeActionType;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookContainerSerializerRulesManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.farmersdelight.recipe.FdCookingPotRecipeSerializer;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class FarmersDelightRecipeInitializer {

    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager ){
        if (Mods.FD.isLoaded) {
            cookRecRecipeInitializerManager.registerCookRecInitializer(new FdCookingPotRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager){
        if (Mods.FD.isLoaded) {
            cookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(ModRecipeTypes.COOKING.get()));
        }
    }

    public static void registerContainerSerializerRule(CookContainerSerializerRulesManager cookContainerSerializerRulesManager){
        if (Mods.FD.isLoaded) {
            cookContainerSerializerRulesManager.registerCookRecSerializerRule(ModRecipeTypes.COOKING.get(), MaidCookBeActionType.CONTAINER);
        }
    }
}
