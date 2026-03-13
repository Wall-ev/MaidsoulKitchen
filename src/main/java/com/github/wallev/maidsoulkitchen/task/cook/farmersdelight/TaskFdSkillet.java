package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.ai.MaidCookMoveTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.inventory.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@TaskClassAnalyzer(com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo.FD_SKILLET)
public class TaskFdSkillet implements ICookTask<SkilletBlockEntity, CampfireCookingRecipe> {
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof SkilletBlockEntity;
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() {
        return RecipeType.CAMPFIRE_COOKING;
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, SkilletBlockEntity blockEntity, MaidRecipesManager<CampfireCookingRecipe> recManager) {
        return !blockEntity.hasStoredStack() && blockEntity.isHeated()
                && !recManager.getRecipesIngredients().isEmpty();
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, SkilletBlockEntity blockEntity, MaidRecipesManager<CampfireCookingRecipe> recManager) {
        // 空实现，实际逻辑在MaidSkilletMakeTask中
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FD_SKILLET.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.SKILLET.get().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.FD_SKILLET;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        if (recipe instanceof CampfireCookingRecipe campfireRecipe) {
            nonNullList.add(campfireRecipe.getIngredients().get(0));
        }
        return nonNullList;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) {
            return Collections.emptyList();
        }

        MaidRecipesManager<CampfireCookingRecipe> skilletRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<SkilletBlockEntity, CampfireCookingRecipe> maidCookMoveTask = new MaidCookMoveTask<>(this, skilletRecipeMaidRecipesManager);
        MaidSkilletMakeTask maidCookMakeTask = new MaidSkilletMakeTask(this, skilletRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }
    
    @Override
    public List<CampfireCookingRecipe> getRecipes(Level level) {
        // 使用接口中已有的getRecipeHolders方法获取所有配方
        return getRecipeHolders(level).stream()
                .map(RecipeHolder::value)
                .collect(Collectors.toList());
    }
}
