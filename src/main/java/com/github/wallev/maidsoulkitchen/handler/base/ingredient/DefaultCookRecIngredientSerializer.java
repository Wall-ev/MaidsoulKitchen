package com.github.wallev.maidsoulkitchen.handler.base.ingredient;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.DefaultCookRec;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class DefaultCookRecIngredientSerializer<R extends Recipe<? extends Container>> extends AbstractCookRecIngredientSerializer<R, DefaultCookRec<R>> {
    public DefaultCookRecIngredientSerializer(RecipeType<R> recipeType) {
        super(recipeType);
    }
}
