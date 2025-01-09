package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minersdelight;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;

public final class MinersDelightRecipeSerializer {

    public static void register() {
        if (Mods.MD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new MdCookingPotRecipeSerializer());
        }
    }

}
