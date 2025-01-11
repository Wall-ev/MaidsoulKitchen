package com.github.wallev.maidsoulkitchen.handler.serializer.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecRecipeSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.serializer.youkaishomecoming.recipe.YhcKettleRecipeSerializer;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;

public class YoukaisHomecomingRecipeSerializer {

    public static void registerRecipeInitializer() {
        if (Mods.YHCD.isLoaded) {
            CookRecRecipeSerializerManager.registerCookRecSerializer(new YhcKettleRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.YHCD.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(YHBlocks.KETTLE_RT.get()));
        }
    }
}
