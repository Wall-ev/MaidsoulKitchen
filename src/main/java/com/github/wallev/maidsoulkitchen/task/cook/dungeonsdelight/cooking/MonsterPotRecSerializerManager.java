package com.github.wallev.maidsoulkitchen.task.cook.dungeonsdelight.cooking;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.yirmiri.dungeonsdelight.common.block.entity.container.MonsterPotRecipe;
import net.yirmiri.dungeonsdelight.core.registry.DDRecipeRegistries;

@TaskClassAnalyzer(TaskInfo.MONSTER_POT)
public class MonsterPotRecSerializerManager extends RecSerializerManager<MonsterPotRecipe> {
    private static final MonsterPotRecSerializerManager INSTANCE = new MonsterPotRecSerializerManager();

    protected MonsterPotRecSerializerManager() {
        super(DDRecipeRegistries.MONSTER_COOKING_RECIPE_TYPE.get());
    }

    public static MonsterPotRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<MonsterPotRecipe> createRecipeInfoProvider() {
        return new MonsterPotRecipeInfo();
    }

    public static class MonsterPotRecipeInfo extends RecipeInfoProvider<MonsterPotRecipe> {

        @Override
        public ItemStack getOutput(RecSerializerManager<MonsterPotRecipe> rsm, MonsterPotRecipe rec) {
            return super.getOutput(rsm, rec);
        }

        @Override
        public ItemStack getContainer(RecSerializerManager<MonsterPotRecipe> rsm, MonsterPotRecipe rec) {
            return rec.getOutputContainer();
        }
    }
}
