package com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.roaster;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.farm_and_charm.core.recipe.RoasterRecipe;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;

import java.util.ArrayList;
import java.util.List;

@TaskClassAnalyzer(TaskInfo.DFC_ROASTER)
public class RoasterRecSerializerManager extends RecSerializerManager<RoasterRecipe> {
    private static final RoasterRecSerializerManager INSTANCE = new RoasterRecSerializerManager();

    protected RoasterRecSerializerManager() {
        super(RecipeTypeRegistry.ROASTER_RECIPE_TYPE.get());
    }

    public static RoasterRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<RoasterRecipe> createRecipeInfoProvider() {
        return new RoasterRecipeInfoProvider();
    }

    public static class RoasterRecipeInfoProvider extends RecipeInfoProvider<RoasterRecipe> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<RoasterRecipe> rsm, RoasterRecipe rec) {
            List<Ingredient> list = new ArrayList<>(rec.getIngredients());
            ItemStack beerCup = rec.getContainer();
            List<RecIngredient> recIngredients = RecIngredient.from(list);
            recIngredients.add(RecIngredient.ofCount(beerCup));
            return recIngredients;
        }
    }
}
