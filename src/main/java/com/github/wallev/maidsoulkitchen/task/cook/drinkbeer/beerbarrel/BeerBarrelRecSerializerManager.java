package com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.beerbarrel;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

@TaskClassAnalyzer(TaskInfo.DB_BEER)
public class BeerBarrelRecSerializerManager extends RecSerializerManager<BrewingRecipe> {
    private static final BeerBarrelRecSerializerManager INSTANCE = new BeerBarrelRecSerializerManager();

    protected BeerBarrelRecSerializerManager() {
        super(RecipeRegistry.RECIPE_TYPE_BREWING.get());
    }

    public static BeerBarrelRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<BrewingRecipe> createRecipeInfoProvider() {
        return new BrewingRecipeInfoProvider();
    }

    public static class BrewingRecipeInfoProvider extends RecipeInfoProvider<BrewingRecipe> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<BrewingRecipe> rsm, BrewingRecipe rec) {
            List<Ingredient> list = new ArrayList<>(rec.getIngredients());
            ItemStack beerCup = rec.getBeerCup();
            List<RecIngredient> recIngredients = RecIngredient.from(list);
            recIngredients.add(RecIngredient.ofCount(beerCup));
            return recIngredients;
        }
    }
}
