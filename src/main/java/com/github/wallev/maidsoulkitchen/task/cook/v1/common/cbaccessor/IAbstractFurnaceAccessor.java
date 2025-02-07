package com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor;

import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IAbstractFurnaceAccessor {
    RecipeType<? extends AbstractCookingRecipe> tlmk$getRecipeType();
}
