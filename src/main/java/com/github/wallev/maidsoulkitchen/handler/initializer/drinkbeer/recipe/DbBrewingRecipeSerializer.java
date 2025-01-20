package com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.DefaultCookRec;
import com.github.wallev.maidsoulkitchen.handler.base.recipe.DefaultCookRecSerializer;
import com.google.common.collect.Lists;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DbBrewingRecipeSerializer extends DefaultCookRecSerializer<BrewingRecipe> {
    public DbBrewingRecipeSerializer() {
        super(RecipeRegistry.RECIPE_TYPE_BREWING.get());
    }

    @Override
    protected void initialize(Level level) {
        this.initRecipes(level);
        for (BrewingRecipe rec : this.recs) {
            List<Ingredient> ingredients = getIngredients(rec);
            ingredients.add(Ingredient.of(rec.getBeerCup()));
            List<Item> resultItem = Lists.newArrayList(getResultItem(rec, level).getItem());
            List<List<Item>> ingreItems = ingredients.stream()
                    .map(ingredient -> {
                        List<Item> itemSet = Arrays.stream(ingredient.getItems())
                                .map(ItemStack::getItem)
                                .collect(Collectors.toList());
                        this.validIngres.addAll(itemSet);
                        return itemSet;
                    })
                    .collect(Collectors.toList());
            DefaultCookRec<BrewingRecipe> cookRec = new DefaultCookRec<>(rec, ingreItems, resultItem);
            this.cookRecs.add(cookRec);
            this.cookRecData.put(rec, cookRec);
        }
    }
}
