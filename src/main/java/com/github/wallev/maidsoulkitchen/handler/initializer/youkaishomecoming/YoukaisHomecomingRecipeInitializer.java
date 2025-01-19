package com.github.wallev.maidsoulkitchen.handler.initializer.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.DefaultCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.youkaishomecoming.recipe.YhcKettleRecipeSerializer;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;

public class YoukaisHomecomingRecipeInitializer {

    public static void registerRecipeInitializer(CookRecRecipeInitializerManager cookRecRecipeInitializerManager ) {
        if (Mods.YHCD.isLoaded) {
            cookRecRecipeInitializerManager.registerCookRecInitializer(new YhcKettleRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(CookRecIngredientSerializerManager cookRecIngredientSerializerManager){
        if (Mods.YHCD.isLoaded) {
            cookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecIngredientSerializer<>(YHBlocks.KETTLE_RT.get()));
        }
    }
}
