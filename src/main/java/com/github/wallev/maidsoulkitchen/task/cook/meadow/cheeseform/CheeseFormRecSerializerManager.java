package com.github.wallev.maidsoulkitchen.task.cook.meadow.cheeseform;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.satisfy.meadow.core.recipes.CheeseFormRecipe;
import net.satisfy.meadow.core.registry.RecipeRegistry;

@TaskClassAnalyzer(TaskInfo.DM_CHEESE_FORM)
public class CheeseFormRecSerializerManager extends RecSerializerManager<CheeseFormRecipe> {
    private static final CheeseFormRecSerializerManager INSTANCE = new CheeseFormRecSerializerManager();

    protected CheeseFormRecSerializerManager() {
        super(RecipeRegistry.CHEESE.get());
    }

    public static CheeseFormRecSerializerManager getInstance() {
        return INSTANCE;
    }
}
