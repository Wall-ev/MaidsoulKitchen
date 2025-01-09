package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.brewinandchewin;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;

public final class BrewinandchewinRecipeSerializer {
    public static void register(){
        if (Mods.BNCD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new BncKeyRecipeSerializer());
        }
    }
}
