package com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor;

import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IAbstractFurnaceAccessor extends IRecipeExperinceAward, ICbeAccessor {
    RecipeType<AbstractCookingRecipe> tlmk$getRecipeType();

    boolean tlmk$isLit();
}
