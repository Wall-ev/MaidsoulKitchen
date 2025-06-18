package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.basin;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.mao.barbequesdelight.content.recipe.SimpleSkeweringRecipe;
import com.mao.barbequesdelight.content.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDRecipes;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class SkeweringRecSerializerManager extends RecSerializerManager<SkeweringRecipe<?>> {
    private static final SkeweringRecSerializerManager INSTANCE = new SkeweringRecSerializerManager();

    protected SkeweringRecSerializerManager() {
        super(BBQDRecipes.RT_SKR.get());
    }

    public static SkeweringRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<SkeweringRecipe<?>> createRecipeInfoProvider() {
        return new SkeweringRecipeInfoProvider();
    }

    public static class SkeweringRecipeInfoProvider extends RecipeInfoProvider<SkeweringRecipe<?>> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<SkeweringRecipe<?>> rsm, SkeweringRecipe<?> rec) {
            SimpleSkeweringRecipe simpleSkeweringRecipe = ((SimpleSkeweringRecipe) rec);

            List<Ingredient> list = new ArrayList<>();
            list.add(simpleSkeweringRecipe.tool);
            list.add(simpleSkeweringRecipe.ingredient);
            Ingredient side = simpleSkeweringRecipe.side;
            if (!side.isEmpty()) {
                list.add(side);
            }

            return RecIngredient.from(list);
        }
    }
}