package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.moka;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.YHC_MOKA)
public class MokaRecSerializerManager extends RecSerializerManager<MokaRecipe> {
    private static final MokaRecSerializerManager INSTANCE = new MokaRecSerializerManager();

    protected MokaRecSerializerManager() {
        super(YHBlocks.MOKA_RT.get());
    }

    public static MokaRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    public String getRecipeTypeId() {
        return YHBlocks.MOKA_RT.getId().toString();
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
