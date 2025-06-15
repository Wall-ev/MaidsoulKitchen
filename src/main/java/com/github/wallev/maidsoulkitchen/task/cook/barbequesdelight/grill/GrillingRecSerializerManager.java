package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill;

import com.github.wallev.maidsoulkitchen.foundation.utility.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.mao.barbequesdelight.content.recipe.GrillingRecipe;
import com.mao.barbequesdelight.content.recipe.SimpleGrillingRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDRecipes;

import java.util.List;

public class GrillingRecSerializerManager extends RecSerializerManager<GrillingRecipe<?>> {
    private static final GrillingRecSerializerManager INSTANCE = new GrillingRecSerializerManager();

    protected GrillingRecSerializerManager() {
        super(BBQDRecipes.RT_BBQ.get());
    }

    public static GrillingRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<GrillingRecipe<?>> createRecipeInfoProvider() {
        return new GrillingRecipeInfoProvider();
    }

    public static class GrillingRecipeInfoProvider extends RecipeInfoProvider<GrillingRecipe<?>> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<GrillingRecipe<?>> rsm, GrillingRecipe<?> rec) {
            return RecIngredient.from(List.of(((SimpleGrillingRecipe) rec).ingredient));
        }
    }
}
