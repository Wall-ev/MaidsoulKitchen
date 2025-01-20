package com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.base.rule.MaidCookBeActionType;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookContainerSerializerRulesManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer.ingredient.DbBrewingRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer.recipe.DbBrewingRecipeSerializer;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;

public class DrinkBeerRecipeInitializer {

    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager) {
        if (Mods.DB.isLoaded) {
            cookRecRecipeInitializerManager.registerCookRecInitializer(new DbBrewingRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager) {
        if (Mods.DB.isLoaded) {
            cookRecIngredientSerializerManager.registerCookRecSerializer(new DbBrewingRecIngredientSerializer());
        }
    }

    public static void registerContainerSerializerRule(CookContainerSerializerRulesManager cookContainerSerializerRulesManager) {
        if (Mods.DB.isLoaded) {
            cookContainerSerializerRulesManager.registerCookRecSerializerRule(RecipeRegistry.RECIPE_TYPE_BREWING.get(), MaidCookBeActionType.DEFAULT);
        }
    }

}
