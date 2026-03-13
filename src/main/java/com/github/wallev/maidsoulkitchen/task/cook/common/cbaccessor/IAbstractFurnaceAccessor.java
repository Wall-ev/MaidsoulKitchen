package com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IAbstractFurnaceAccessor extends IMskMixinInterface {
    RecipeType<? extends AbstractCookingRecipe> tlmk$getRecipeType();
}
