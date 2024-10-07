package com.github.catbert.tlma.api.task.v1.cook;

import com.github.catbert.tlma.task.cook.v1.common.bestate.IBaseCookContainerBe;
import com.github.catbert.tlma.task.cook.v1.common.bestate.IHeatBe;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public interface IBaseContainerPotCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends IBaseCookContainerBe<B, R>, IHeatBe<B>, IContainerCookBe<B>, IContainerCook {

    default boolean maidShouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        Container inventory = getContainer(blockEntity);
//        ItemStack outputStack = inventory.getItem(getOutputSlot());
//        // 有最终物品
////        LOGGER.info("outputStack: {} ", outputStack);
//        if (!outputStack.isEmpty()) {
//            return true;
//        }
        if (canTakeOutput(inventory, blockEntity)) {
            return true;
        }

        boolean heated = isHeated(blockEntity);
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = beInnerCanCook(inventory, blockEntity);
        List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = maidRecipesManager.getRecipesIngredients();
//        LOGGER.info("recipe: {} {}",  b, recipesIngredients);
        if (!b && !recipesIngredients.isEmpty() && heated) {
            return true;
        }

        // 能做饭现在和有输入（也就是厨锅现在有物品再里面但是不符合配方
//        LOGGER.info("hasInput: {} {}", b, hasInput(inventory));
        if (inputCanTake(b, inventory)) {
            return true;
        }

        return false;
    }

    default boolean inputCanTake(boolean beInnerCanCook, Container inventory){
        return !beInnerCanCook && hasInput(inventory);
    }

    default boolean canTakeOutput(Container inventory, B be) {
        // 有最终物品
        return !inventory.getItem(getOutputSlot()).isEmpty();
    }

    default void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
//        LOGGER.info("MaidCookMakeTask.processCookMake：");
//        LOGGER.info("maidRecipesManager: {} ", maidRecipesManager);
//        LOGGER.info("getRecipesIngredients: {} ", maidRecipesManager.getRecipesIngredients());

        tryExtractItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);

        tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);

        maidRecipesManager.getLastInv().syncInv();
    }

    default void tryExtractItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        Container inventory = getContainer(blockEntity);
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        // 取出最终物品
        extractOutputStack(inventory, maidRecipesManager.getOutputInv(entityMaid), blockEntity);


        boolean heated = isHeated(blockEntity);
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = beInnerCanCook(inventory, blockEntity);
        if (inputCanTake(b, inventory)) {
            extractInputStack(inventory, maidRecipesManager.getIngreInv(entityMaid), blockEntity);
        }

        pickupAction(entityMaid);
    }


    default void tryInsertItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        Container inventory = getContainer(blockEntity);
        Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecipesManager.getRecipeIngredient();
        if (hasInput(inventory) || recipeIngredient == null) return;

        insertInputStack(inventory, availableInv, blockEntity, recipeIngredient);

        pickupAction(entityMaid);
    }

    boolean beInnerCanCook(Container inventory, B be);
}
