package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.grape;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.IRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface IGrapeJumpRecipeGuideGenerator<R extends Recipe<? extends Container>> extends IGrapeJumpCustomGuideGenerator<R>, IRecipeGuideGenerator<R> {

    @Override
    default ResourceLocation getRecipeId(R recipe) {
        return recipe.getId();
    }

    @Override
    @NotNull
    default ResourceLocation getType() {
        return VResourceLocation.createTypeMod(getRecipeType().toString());
    }

    @Override
    default void consumeRecipes(RecipeManager manager, Consumer<R> recipeConsumer) {
        IRecipeGuideGenerator.super.consumeRecipes(manager, recipeConsumer);
    }

    default List<Ingredient> getInputs(R recipe) {
        return IRecipeGuideGenerator.super.getInputs(recipe);
    }

    default List<ItemStack> getOutputs(R recipe, RegistryAccess registryAccess) {
        return IRecipeGuideGenerator.super.getOutputs(recipe, registryAccess);
    }
}
