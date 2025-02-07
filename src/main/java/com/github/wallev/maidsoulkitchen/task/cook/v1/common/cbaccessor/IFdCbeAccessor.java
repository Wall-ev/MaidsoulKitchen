package com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IFdCbeAccessor<R extends Recipe<? extends Container>> {

    Optional<R> tlmk$getMatchingRecipe(RecipeWrapper inventoryWrapper);

    boolean tlmk$canCook(R recipe);

}
