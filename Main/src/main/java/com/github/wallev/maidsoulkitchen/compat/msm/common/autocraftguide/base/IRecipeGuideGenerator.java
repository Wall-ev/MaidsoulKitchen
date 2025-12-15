package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.function.Consumer;

public interface IRecipeGuideGenerator<R extends Recipe<? extends Container>> {

    RecipeType<R> getRecipeType();

    @SuppressWarnings("all")
    default void consumeRecipes(RecipeManager manager, Consumer<R> recipeConsumer) {
        RecipeType<R> recipeType = this.getRecipeType();
        manager.getAllRecipesFor((RecipeType) recipeType).forEach(recipe -> {
            recipeConsumer.accept((R) recipe);
        });
    }

    /**
     * 获取配方id
     * @param recipe 配方
     * @return 配方id
     */
    default ResourceLocation getRecipeId(R recipe) {
        return recipe.getId();
    }

    /**
     * 获取配方输入
     * @param recipe 配方
     * @return 配方输入
     */
    default List<Ingredient> getInputs(R recipe) {
        return recipe.getIngredients();
    }

    /**
     * 获取配方输出
     * @param recipe 配方
     * @param registryAccess 注册表访问
     * @return 配方输出
     */
    default List<ItemStack> getOutputs(R recipe, RegistryAccess registryAccess) {
        return List.of(recipe.getResultItem(registryAccess));
    }

}
