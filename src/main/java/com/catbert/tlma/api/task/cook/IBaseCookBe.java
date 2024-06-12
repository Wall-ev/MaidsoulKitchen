package com.catbert.tlma.api.task.cook;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IBaseCookBe<T extends Recipe<? extends Container>, C extends BlockEntity> {

    ItemStackHandler getItemStackHandler(C be);

    Optional<T> getMatchingRecipe(C be, RecipeWrapper recipeWrapper);

    boolean canCook(C be, T recipe);
}
