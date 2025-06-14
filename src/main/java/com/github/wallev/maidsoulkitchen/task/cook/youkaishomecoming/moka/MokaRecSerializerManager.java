package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.moka;

import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.ItemStack;

public class MokaRecSerializerManager extends RecSerializerManager<MokaRecipe> {
    public MokaRecSerializerManager() {
        super(YHBlocks.MOKA_RT.get());
    }

    @Override
    protected RecipeInfoProvider<MokaRecipe> createRecipeInfoProvider() {
        return new MokaRecipeInfo();
    }

    public static class MokaRecipeInfo extends RecipeInfoProvider<MokaRecipe> {
        @Override
        public ItemStack getContainer(RecSerializerManager<MokaRecipe> rsm, MokaRecipe rec) {
            return rec.getOutputContainer();
        }


    }
}
