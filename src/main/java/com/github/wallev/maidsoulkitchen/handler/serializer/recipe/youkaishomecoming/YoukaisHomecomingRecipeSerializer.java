package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;

public final class YoukaisHomecomingRecipeSerializer {

    public static void register() {
        if (Mods.YHCD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new YhcKettleRecipeSerializer());
        }
    }

}
