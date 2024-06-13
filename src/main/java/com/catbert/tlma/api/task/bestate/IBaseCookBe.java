package com.catbert.tlma.api.task.bestate;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IBaseCookBe<B extends BlockEntity, R extends Recipe<? extends Container>> {

    ItemStackHandler getItemStackHandler(B be);

    Optional<R> getMatchingRecipe(B be, RecipeWrapper recipeWrapper);

    boolean canCook(B be, R recipe);
}
