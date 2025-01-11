package com.github.wallev.maidsoulkitchen.handler.base.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.DefaultCookRec;
import com.google.common.collect.Lists;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultCookRecSerializer<R extends Recipe<? extends Container>> extends AbstractCookRecSerializer<R> {
    public DefaultCookRecSerializer(RecipeType<R> recipeType) {
        super(recipeType);
    }

    @Override
    protected void initialize(Level level) {
        this.initRecipes(level);
        for (R rec : this.cookRecs) {
            List<Ingredient> ingredients = getIngredients(rec);
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
            DefaultCookRec<R> cookRec = new DefaultCookRec<>(rec, ingreItems, resultItem);
            this.cookRecData.put(rec, cookRec);
        }
    }
}
