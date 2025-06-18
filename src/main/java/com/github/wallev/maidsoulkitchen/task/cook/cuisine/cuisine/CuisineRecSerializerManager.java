package com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.content.recipe.CuisineRecipeMatch;
import dev.xkmc.cuisinedelight.init.registrate.CDMisc;

import java.util.List;

public class CuisineRecSerializerManager extends RecSerializerManager<BaseCuisineRecipe<?>> {
    private static final CuisineRecSerializerManager INSTANCE = new CuisineRecSerializerManager();

    protected CuisineRecSerializerManager() {
        super(CDMisc.RT_CUISINE.get());
    }

    public static CuisineRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<BaseCuisineRecipe<?>> createRecipeInfoProvider() {
        return new CuisineRecipeInfoProvider();
    }

    public static class CuisineRecipeInfoProvider extends RecipeInfoProvider<BaseCuisineRecipe<?>> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<BaseCuisineRecipe<?>> rsm, BaseCuisineRecipe<?> rec) {
            return rec.list.stream()
                    .map(CuisineRecipeMatch::ingredient)
                    .map(RecIngredient::of)
                    .toList();
        }

        @Override
        public boolean isSingle(RecSerializerManager<BaseCuisineRecipe<?>> rsm, BaseCuisineRecipe<?> rec) {
            return true;
        }
    }
}
