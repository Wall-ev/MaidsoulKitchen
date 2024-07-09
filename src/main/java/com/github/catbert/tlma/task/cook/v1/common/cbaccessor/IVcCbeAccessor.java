package com.github.catbert.tlma.task.cook.v1.common.cbaccessor;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IVcCbeAccessor<R extends Recipe<? extends Container>> {

    Optional<R> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper);

    boolean canCook$tlma(R recipe);

}
