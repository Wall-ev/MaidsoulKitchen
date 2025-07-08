package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.kettle;

import com.github.wallev.maidsoulkitchen.mixin.compat.youkaishomecoming.KettleBlockAccessor;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TaskClassAnalyzer(TaskInfo.YHC_TEA_KETTLE)
public class KettleRecSerializerManager extends RecSerializerManager<KettleRecipe> {
    private static final KettleRecSerializerManager INSTANCE = new KettleRecSerializerManager();

    protected KettleRecSerializerManager() {
        super(YHBlocks.KETTLE_RT.get());
    }

    public static KettleRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    public String getRecipeTypeId() {
        return YHBlocks.KETTLE_RT.getId().toString();
    }

    @Override
    protected void initFuels() {
        Lazy<Map<Ingredient, Integer>> waters = KettleBlockAccessor.waters();
        Map<Ingredient, Integer> map = waters.get();
        List<ItemStack> list = map.keySet().stream()
                .flatMap(k -> Arrays.stream(k.getItems()))
                .toList();
        this.fuels = list;
    }

    @Override
    protected RecipeInfoProvider<KettleRecipe> createRecipeInfoProvider() {
        return new KettleRecipeInfoProvider();
    }

    public static class KettleRecipeInfoProvider extends RecipeInfoProvider<KettleRecipe> {
        @Override
        public ItemStack getContainer(RecSerializerManager<KettleRecipe> rsm, KettleRecipe rec) {
            return rec.getOutputContainer();
        }
    }
}
