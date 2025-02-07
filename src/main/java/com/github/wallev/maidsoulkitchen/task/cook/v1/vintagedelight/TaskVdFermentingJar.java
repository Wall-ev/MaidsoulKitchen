package com.github.wallev.maidsoulkitchen.task.cook.v1.vintagedelight;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.ribs.vintagedelight.block.entity.FermentingJarBlockEntity;
import net.ribs.vintagedelight.recipe.FermentingRecipe;

public class TaskVdFermentingJar implements ICookTask<FermentingJarBlockEntity, FermentingRecipe> {
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof FermentingJarBlockEntity;
    }

    @Override
    public RecipeType<FermentingRecipe> getRecipeType() {
        return FermentingRecipe.Type.INSTANCE;
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, FermentingJarBlockEntity blockEntity, MaidRecipesManager<FermentingRecipe> recManager) {
        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, FermentingJarBlockEntity blockEntity, MaidRecipesManager<FermentingRecipe> recManager) {

    }

    @Override
    public ResourceLocation getUid() {
        return null;
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return null;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        NonNullList<Ingredient> recIngredients = ((FermentingRecipe) recipe).getIngredients();
        ingredients.addAll(recIngredients);
        ingredients.add(Ingredient.of(((FermentingRecipe) recipe).getContainerItemStack()));

        return ICookTask.super.getIngredients(recipe);
    }
}
