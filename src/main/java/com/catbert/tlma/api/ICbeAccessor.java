package com.catbert.tlma.api;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface ICbeAccessor<R extends Recipe<? extends Container>> {

    Optional<R> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper);

    boolean canCook$tlma(R recipe);

}
