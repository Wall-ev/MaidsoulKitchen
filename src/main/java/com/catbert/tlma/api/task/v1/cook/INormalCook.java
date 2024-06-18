package com.catbert.tlma.api.task.v1.cook;

import com.catbert.tlma.api.task.v1.bestate.IBaseCookItemHandlerBe;
import com.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
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

import static com.catbert.tlma.TLMAddon.LOGGER;

public interface INormalCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends IBaseCookItemHandlerBe<B, R>, IHandlerCookBe<B>, IItemHandlerCook {

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
//        LOGGER.info("MaidCookMakeTask.processCookMake：");
//        LOGGER.info("maidRecipesManager: {} ", maidRecipesManager);
//        LOGGER.info("getRecipesIngredients: {} ", maidRecipesManager.getRecipesIngredients());

        tryExtractItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);

        tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    default void tryInsertItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecipesManager.getRecipeIngredient();
        if (recipeIngredient == null) return;

        insertInputStack(inventory, availableInv, blockEntity, recipeIngredient);

        pickupAction(entityMaid);

    }

    default void tryExtractItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        // 取出最终物品
        extractOutputStack(inventory, availableInv, blockEntity);

        Optional<R> recipe = getMatchingRecipe(blockEntity, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(blockEntity, recipe.get());
        if (!b && hasInput(inventory)) {
            extractInputStack(inventory, availableInv, blockEntity);
        }

        pickupAction(entityMaid);
    }
}
