package com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.stove;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.satisfy.farm_and_charm.core.recipe.StoveRecipe;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;

@TaskClassAnalyzer(TaskInfo.DFC_STOVE)
public class StoveRecSerializerManager extends RecSerializerManager<StoveRecipe> {
    private static final StoveRecSerializerManager INSTANCE = new StoveRecSerializerManager();

    protected StoveRecSerializerManager() {
        super(RecipeTypeRegistry.STOVE_RECIPE_TYPE.get());
    }

    public static StoveRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initFuels() {
        this.fuels = createDefaultFuels();
    }

}
