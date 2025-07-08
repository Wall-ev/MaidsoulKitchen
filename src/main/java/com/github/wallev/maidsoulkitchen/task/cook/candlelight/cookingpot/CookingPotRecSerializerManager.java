package com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpot;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;

import java.util.ArrayList;
import java.util.List;

@TaskClassAnalyzer(TaskInfo.DCL_COOKING_POT)
public class CookingPotRecSerializerManager extends RecSerializerManager<CookingPotRecipe> {
    private static final CookingPotRecSerializerManager INSTANCE = new CookingPotRecSerializerManager();

    protected CookingPotRecSerializerManager() {
        super(RecipeTypeRegistry.COOKING_POT_RECIPE_TYPE.get());
    }

    public static CookingPotRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<CookingPotRecipe> createRecipeInfoProvider() {
        return new CookingPotRecipeInfoProvider();
    }

    public static class CookingPotRecipeInfoProvider extends RecipeInfoProvider<CookingPotRecipe> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<CookingPotRecipe> rsm, CookingPotRecipe rec) {
            List<Ingredient> list = new ArrayList<>(rec.getIngredients());
            ItemStack beerCup = rec.getContainerItem();
            List<RecIngredient> recIngredients = RecIngredient.from(list);
            recIngredients.add(RecIngredient.ofCount(beerCup));
            return recIngredients;
        }
    }
}
