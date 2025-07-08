package com.github.wallev.maidsoulkitchen.task.cook.beachparty.minifridge;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.RecipeRegistry;

@TaskClassAnalyzer(TaskInfo.DBP_MINI_FRIDGE)
public class MiniFridgeRecSerializerManager extends RecSerializerManager<MiniFridgeRecipe> {
    private static final MiniFridgeRecSerializerManager INSTANCE = new MiniFridgeRecSerializerManager();

    protected MiniFridgeRecSerializerManager() {
        super(RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get());
    }

    public static MiniFridgeRecSerializerManager getInstance() {
        return INSTANCE;
    }
}
