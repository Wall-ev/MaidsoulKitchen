package com.github.wallev.maidsoulkitchen.task.cook.v1.cuisine;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.ai.MaidCookMoveTask;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.content.recipe.CuisineRecipeMatch;
import dev.xkmc.cuisinedelight.init.registrate.CDBlocks;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import dev.xkmc.cuisinedelight.init.registrate.CDMisc;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.Collections;
import java.util.List;

public class TaskCdCuisineSkillet implements ICookTask<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> {
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CuisineSkilletBlockEntity;
    }

    @Override
    public RecipeType<BaseCuisineRecipe<?>> getRecipeType() {
        return CDMisc.RT_CUISINE.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, CuisineSkilletBlockEntity blockEntity, MaidRecipesManager<BaseCuisineRecipe<?>> recManager) {
        CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);
        return !blockEntity.isCooking() && blockEntity.canCook()
                && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.SPATULA.get())) > -1
                && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.PLATE.get())) > -1
                && !recManager.getRecipesIngredients().isEmpty();
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, CuisineSkilletBlockEntity blockEntity, MaidRecipesManager<BaseCuisineRecipe<?>> recManager) {

    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.CD_CUISINE_SKILLET.uid;
    }

    @Override
    public ItemStack getIcon() {
        return CDBlocks.SKILLET.asStack();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.CUISINE_SKILLET;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        List<Ingredient> list = ((BaseCuisineRecipe<?>) recipe).list.stream().map(CuisineRecipeMatch::ingredient).toList();
        nonNullList.addAll(list);
        return nonNullList;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) {
            return Collections.emptyList();
        }

        MaidRecipesManager<BaseCuisineRecipe<?>> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> maidCookMoveTask = new MaidCookMoveTask<>(this, cookingPotRecipeMaidRecipesManager);
        MaidCuisineMakeTask maidCookMakeTask = new MaidCuisineMakeTask(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }
}
