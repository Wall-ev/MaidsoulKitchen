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
import com.mao.barbequesdelight.content.block.GrillBlockEntity;
import com.mao.barbequesdelight.content.recipe.GrillingRecipe;
import com.mao.barbequesdelight.content.recipe.SimpleGrillingRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import com.mao.barbequesdelight.init.registrate.BBQDItems;
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

public class TaskBdGrill implements ICookTask<GrillBlockEntity, GrillingRecipe<?>> {
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof GrillBlockEntity;
    }

    @Override
    public RecipeType<GrillingRecipe<?>> getRecipeType() {
        return BBQDRecipes.RT_BBQ.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, GrillBlockEntity blockEntity, MaidRecipesManager<GrillingRecipe<?>> recManager) {
        boolean innerCanCook = false;

        // 取出烤焦的食物
        GrillBlockEntity.ItemEntry[] itemEntries = blockEntity.entries;
        for (GrillBlockEntity.ItemEntry itemEntry : itemEntries) {
            if (itemEntry.stack.is(BBQDItems.BURNT_FOOD.asItem())) {
                return true;
            } else if (!itemEntry.stack.isEmpty()) {

                // 要翻转了
                if (itemEntry.canFlip()) {
                    return true;
                }

                if (itemEntry.flipped && itemEntry.time >= itemEntry.duration) {
                    return true;
                }

                innerCanCook = true;
            }
        }

        // 烧烤架没有在烤东西，并且女仆身上有待烧烤的食物
        if (blockEntity.isHeated() && !innerCanCook && !recManager.getRecipesIngredients().isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, GrillBlockEntity blockEntity, MaidRecipesManager<GrillingRecipe<?>> recManager) {

    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) {
            return Collections.emptyList();
        }

        MaidRecipesManager<GrillingRecipe<?>> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<GrillBlockEntity, GrillingRecipe<?>> maidCookMoveTask = new MaidCookMoveTask<>(this, cookingPotRecipeMaidRecipesManager);
        MaidGrillMakeTask maidGrillMakeTask = new MaidGrillMakeTask(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidGrillMakeTask));
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BD_GRILL.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BBQDBlocks.GRILL.asStack();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.BD_GRILL;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        SimpleGrillingRecipe simpleGrillingRecipe = (SimpleGrillingRecipe) recipe;
        ingredients.add(simpleGrillingRecipe.ingredient);
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(Recipe<?> recipe, RegistryAccess pRegistryAccess) {
        return ICookTask.super.getResultItem(recipe, pRegistryAccess);
    }
}
