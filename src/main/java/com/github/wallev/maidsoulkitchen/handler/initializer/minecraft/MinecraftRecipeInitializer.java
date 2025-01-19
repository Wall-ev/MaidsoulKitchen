package com.github.wallev.maidsoulkitchen.handler.initializer.minecraft;

import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.minecraft.recipe.BlastingRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.minecraft.recipe.SmeltingRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.minecraft.recipe.SmokingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MinecraftRecipeInitializer {

    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager ) {
        cookRecRecipeInitializerManager.registerCookRecInitializer(new BlastingRecipeSerializer());
        cookRecRecipeInitializerManager.registerCookRecInitializer(new SmokingRecipeSerializer());
        cookRecRecipeInitializerManager.registerCookRecInitializer(new SmeltingRecipeSerializer());
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager) {
        cookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(RecipeType.BLASTING));
        cookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(RecipeType.SMOKING));
        cookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(RecipeType.SMELTING));
    }

}
