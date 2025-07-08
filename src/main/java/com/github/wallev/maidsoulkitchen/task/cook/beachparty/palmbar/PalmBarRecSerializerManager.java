package com.github.wallev.maidsoulkitchen.task.cook.beachparty.palmbar;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.RecipeRegistry;

@TaskClassAnalyzer(TaskInfo.DBP_PALM_BAR)
public class PalmBarRecSerializerManager extends RecSerializerManager<PalmBarRecipe> {
    private static final PalmBarRecSerializerManager INSTANCE = new PalmBarRecSerializerManager();

    protected PalmBarRecSerializerManager() {
        super(RecipeRegistry.PALM_BAR_RECIPE_TYPE.get());
    }

    public static PalmBarRecSerializerManager getInstance() {
        return INSTANCE;
    }
}
