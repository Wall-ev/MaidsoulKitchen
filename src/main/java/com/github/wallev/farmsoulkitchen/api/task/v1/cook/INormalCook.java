package com.github.wallev.farmsoulkitchen.api.task.v1.cook;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.bestate.IBaseCookItemHandlerBe;
import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

public interface INormalCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends IBaseCookItemHandlerBe<B, R>, IHandlerCookBe<B>, IItemHandlerCook<B, R> {

    default boolean maidShouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        ItemStack outputStack = inventory.getStackInSlot(getOutputSlot());
        // 有最终物品
//        LOGGER.info("outputStack: {} ", outputStack);
        if (!outputStack.isEmpty()) {
            return true;
        }

        Optional<R> recipe = getMatchingRecipe(blockEntity, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(blockEntity, recipe.get());
        List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = maidRecipesManager.getRecipesIngredients();
//        LOGGER.info("recipe: {} {} {} ", recipe, b, recipesIngredients);
        if (!b && !recipesIngredients.isEmpty()) {
            return true;
        }

        // 能做饭现在和有输入（也就是厨锅现在有物品再里面但是不符合配方
//        LOGGER.info("hasInput: {} {}", b, hasInput(inventory));
        if (!b && hasInput(inventory)) {
            return true;
        }

        return false;
    }

    default void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        tryExtractItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
        tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);

        maidRecipesManager.getCookInv().syncInv();
    }

    default void tryInsertItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecipesManager.getRecipeIngredient();
        if (recipeIngredient.getFirst().isEmpty()) return;

        insertInputsStack(inventory, availableInv, blockEntity, recipeIngredient);

        pickupAction(entityMaid);
    }

    default void tryExtractItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        // 取出最终物品
        extractOutputStack(inventory, maidRecipesManager.getOutputInv(), blockEntity);

        Optional<R> recipe = getMatchingRecipe(blockEntity, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(blockEntity, recipe.get());
        if (!b && hasInput(inventory)) {
            extractInputsStack(inventory, maidRecipesManager.getInputInv(), blockEntity);
        }

        pickupAction(entityMaid);
    }
}
