package com.github.wallev.maidsoulkitchen.task.cook.v1.barbequesdelight;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.ai.MaidCookMoveTask;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.mao.barbequesdelight.content.block.BasinBlockEntity;
import com.mao.barbequesdelight.content.recipe.SimpleSkeweringRecipe;
import com.mao.barbequesdelight.content.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import com.mao.barbequesdelight.init.registrate.BBQDRecipes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.List;

public class TaskBdBasin implements ICookTask<BasinBlockEntity, SkeweringRecipe<?>> {
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof BasinBlockEntity;
    }

    @Override
    public RecipeType<SkeweringRecipe<?>> getRecipeType() {
        return BBQDRecipes.RT_SKR.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, BasinBlockEntity blockEntity, MaidRecipesManager<SkeweringRecipe<?>> recManager) {
        if (!blockEntity.items.isEmpty()) {
            return true;
        }

        if (!recManager.getRecipesIngredients().isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, BasinBlockEntity blockEntity, MaidRecipesManager<SkeweringRecipe<?>> recManager) {

    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) {
            return Collections.emptyList();
        }

        MaidRecipesManager<SkeweringRecipe<?>> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<BasinBlockEntity, SkeweringRecipe<?>> maidCookMoveTask = new MaidCookMoveTask<>(this, cookingPotRecipeMaidRecipesManager);
        MaidBasinMakeTask maidBasinMakeTask = new MaidBasinMakeTask(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidBasinMakeTask));
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BD_BASIN.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BBQDBlocks.BASIN.asStack();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.BD_BASIN;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        SimpleSkeweringRecipe simpleSkeweringRecipe = (SimpleSkeweringRecipe) recipe;
        ingredients.add(simpleSkeweringRecipe.ingredient);
        ingredients.add(simpleSkeweringRecipe.tool);
        if (!simpleSkeweringRecipe.side.isEmpty()) {
            ingredients.add(simpleSkeweringRecipe.side);
        }

        return ingredients;
    }

    @Override
    public ItemStack getResultItem(Recipe<?> recipe, RegistryAccess pRegistryAccess) {
        return ICookTask.super.getResultItem(recipe, pRegistryAccess);
    }
}
