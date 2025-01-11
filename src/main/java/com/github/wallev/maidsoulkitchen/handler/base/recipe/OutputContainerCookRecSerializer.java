package com.github.wallev.maidsoulkitchen.handler.base.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.ContainerCookRec;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class OutputContainerCookRecSerializer<R extends Recipe<? extends Container>> extends AbstractCookRecSerializer<R> {
    protected final Set<Item> validContainers = Sets.newHashSet();
    public OutputContainerCookRecSerializer(RecipeType<R> recipeType) {
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
            Item container = getContainer(rec).getItem();
            this.validContainers.add(container);
            ContainerCookRec<R> cookRec = new ContainerCookRec<>(rec, ingreItems, resultItem, container);
            this.cookRecData.put(rec, cookRec);
        }
    }

    public Set<Item> getValidContainers() {
        return validContainers;
    }

    public abstract ItemStack getContainer(R recipe);
}
