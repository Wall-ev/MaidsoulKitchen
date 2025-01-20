package com.github.wallev.maidsoulkitchen.handler.initializer.brewinandchewin.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.ContainerCookRec;
import com.github.wallev.maidsoulkitchen.handler.base.recipe.OutputContainerCookRecSerializer;
import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BncKeyRecipeSerializer extends OutputContainerCookRecSerializer<KegRecipe> {
    public BncKeyRecipeSerializer() {
        super(BCRecipeTypes.FERMENTING.get());
    }

    @Override
    protected void initialize(Level level) {
        this.initRecipes(level);
        for (KegRecipe rec : this.recs) {
            List<Ingredient> ingredients = getIngredients(rec);

            // 饮酒作乐里的ingredient里包含了fluidItem...
            // 实在不明白为啥要把fluidItem加入ingredient里，
            // 然后又在需要配方判断的时候把fluidItem去掉....
            int size = ingredients.size();
            int il = 5 - size;
            if (il > 0) {
                for (int i = 0; i < il; i++) {
                    ingredients.add(size - 1 + i , Ingredient.EMPTY);
                }
            }

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
            ContainerCookRec<KegRecipe> cookRec = new ContainerCookRec<>(rec, ingreItems, resultItem, container);
            this.cookRecs.add(cookRec);
            this.cookRecData.put(rec, cookRec);
        }
    }

    @Override
    public ItemStack getContainer(KegRecipe recipe) {
        return recipe.getOutputContainer();
    }
}
