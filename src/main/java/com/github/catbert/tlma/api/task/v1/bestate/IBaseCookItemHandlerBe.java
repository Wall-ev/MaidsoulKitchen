package com.github.catbert.tlma.api.task.v1.bestate;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IBaseCookItemHandlerBe<B extends BlockEntity, R extends Recipe<? extends Container>> {
    Optional<R> getMatchingRecipe(B be, RecipeWrapper recipeWrapper);

    boolean canCook(B be, R recipe);
}
