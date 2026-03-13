package com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IFdCbeAccessor<R extends Recipe<? extends RecipeInput>> extends IMskMixinInterface {

    Optional<R> tlmk$getMatchingRecipe(RecipeWrapper inventoryWrapper);

    boolean tlmk$canCook(R recipe);

}
