package com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace;

import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IAbstractFurnaceAccessor {
    RecipeType<AbstractCookingRecipe> tlmk$getRecipeType();

    boolean tlmk$isLit();
}
