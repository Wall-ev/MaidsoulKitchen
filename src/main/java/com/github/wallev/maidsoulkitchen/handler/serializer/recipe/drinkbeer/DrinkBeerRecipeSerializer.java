package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.drinkbeer;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.rule.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.rule.common.DefaultCookRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.rule.drinkbeer.DbBrewingRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft.SmeltingRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft.SmokingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class DrinkBeerRecipeSerializer {

    public static void registerRecipeInitializer() {
        if (Mods.DB.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new DbBrewingRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer() {
        if (Mods.DB.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DbBrewingRecRuleSerializer());
        }
    }

}
