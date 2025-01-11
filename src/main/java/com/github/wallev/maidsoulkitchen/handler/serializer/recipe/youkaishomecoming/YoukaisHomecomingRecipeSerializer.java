package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.handler.rule.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.rule.common.DefaultCookRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.CookRecipeSerializerManager;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;

public final class YoukaisHomecomingRecipeSerializer {

    public static void registerRecipeInitializer() {
        if (Mods.YHCD.isLoaded) {
            CookRecipeSerializerManager.registerCookRecSerializer(new YhcKettleRecipeSerializer());
        }
    }

    public static void registerIngredientSerializer(){
        if (Mods.YHCD.isLoaded) {
            CookRecIngredientSerializerManager.registerCookRecSerializer(new DefaultCookRecRuleSerializer<>(YHBlocks.KETTLE_RT.get()));
        }
    }
}
