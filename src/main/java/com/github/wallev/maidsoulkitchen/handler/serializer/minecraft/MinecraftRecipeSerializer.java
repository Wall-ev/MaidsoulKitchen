package com.github.wallev.maidsoulkitchen.handler.serializer.minecraft;

import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.minecraft.recipe.BlastingRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.minecraft.recipe.SmeltingRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.minecraft.recipe.SmokingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MinecraftRecipeSerializer {

    public static void registerRecipeInitializer() {
        CookRecRecipeSerializerManager.registerCookRecSerializer(new BlastingRecipeSerializer());
        CookRecRecipeSerializerManager.registerCookRecSerializer(new SmokingRecipeSerializer());
        CookRecRecipeSerializerManager.registerCookRecSerializer(new SmeltingRecipeSerializer());
    }

    public static void registerIngredientSerializer() {
        CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(RecipeType.BLASTING));
        CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(RecipeType.SMOKING));
        CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(RecipeType.SMELTING));
    }

}
