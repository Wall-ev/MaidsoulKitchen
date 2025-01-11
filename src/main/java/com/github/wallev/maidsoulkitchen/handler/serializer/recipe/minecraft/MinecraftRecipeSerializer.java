package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft;

import com.github.wallev.maidsoulkitchen.handler.rule.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.rule.common.DefaultCookRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;
import net.minecraft.world.item.crafting.RecipeType;

public final class MinecraftRecipeSerializer {

    public static void registerRecipeInitializer() {
        CookRecipeSerializerManager.registerCookRecSerializer(new BlastingRecipeSerializer());
        CookRecipeSerializerManager.registerCookRecSerializer(new SmokingRecipeSerializer());
        CookRecipeSerializerManager.registerCookRecSerializer(new SmeltingRecipeSerializer());
    }

    public static void registerIngredientSerializer() {
        CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecRuleSerializer<>(RecipeType.BLASTING));
        CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecRuleSerializer<>(RecipeType.SMOKING));
        CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecRuleSerializer<>(RecipeType.SMELTING));
    }

}
