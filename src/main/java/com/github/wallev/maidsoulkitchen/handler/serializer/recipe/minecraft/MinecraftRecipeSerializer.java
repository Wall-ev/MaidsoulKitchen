package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft;

import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;

public final class MinecraftRecipeSerializer {

    public static void register(){
        CookRecipeSerializerManager.registerCookRecSerializer(new BlastingRecipeSerializer());
        CookRecipeSerializerManager.registerCookRecSerializer(new SmokingRecipeSerializer());
        CookRecipeSerializerManager.registerCookRecSerializer(new SmeltingRecipeSerializer());
    }

}
