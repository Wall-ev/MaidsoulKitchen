package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.farmersdelight;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;

public final class FarmersDelightRecipeSerializer {

    public static void register(){
        if (Mods.FD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new FdCookingPotRecipeSerializer());
        }
    }

}
