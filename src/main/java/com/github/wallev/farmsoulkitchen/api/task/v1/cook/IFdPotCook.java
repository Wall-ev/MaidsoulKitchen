package com.github.wallev.farmsoulkitchen.api.task.v1.cook;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.bestate.IBaseCookItemHandlerBe;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.bestate.IHeatBe;
import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface IFdPotCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends IBaseCookItemHandlerBe<B, R>, IHeatBe<B>, IHandlerCookBe<B>, IItemHandlerCook<B, R> {

    int getMealStackSlot();

    int getContainerStackSlot();

    ItemStack getFoodContainer(B blockEntity);

    default boolean maidShouldMoveTo(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> manager) {
        ItemStackHandler inventory = getItemStackHandler(be);
        ItemStack outputStack = inventory.getStackInSlot(getOutputSlot());
        // 有最终物品
        if (!outputStack.isEmpty()) {
            return true;
        }

        ItemStack mealStack = getBeInvMealStack(be, inventory);
        ItemStack container = getFoodContainer(be);
        boolean hasOutputAdditionItem = manager.hasOutputAdditionItem(container);
        // 有待取出物品和对应的容器
        if (!mealStack.isEmpty() && hasOutputAdditionItem) {
            return true;
        }

        boolean heated = isHeated(be);
        Optional<R> recipe = getMatchingRecipe(be, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(be, recipe.get());
        List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = manager.getRecipesIngredients();
        if (!b && !recipesIngredients.isEmpty() && heated && mealStack.isEmpty()) {
            return true;
        }

        // 能做饭现在和有输入（也就是厨锅现在有物品再里面但是不符合配方
        if (!b && hasInput(inventory)) {
            return true;
        }

        ItemStack containerInputStack = inventory.getStackInSlot(getContainerStackSlot());
        //当厨锅没有物品，又有杯具在时，就取出杯具
        if (!hasInput(inventory) && !containerInputStack.isEmpty()) {
            return true;
        }

        return false;
    }

    @NotNull
    default ItemStack getBeInvMealStack(B be, ItemStackHandler inventory) {
        return inventory.getStackInSlot(getMealStackSlot());
    }

    default void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        tryExtractItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
        tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);

        maidRecipesManager.getCookInv().syncInv();
    }

    default void tryInsertItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        ItemStack mealStack = getBeInvMealStack(blockEntity, inventory);
        Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecipesManager.getRecipeIngredient();
        if (hasInput(inventory) || !mealStack.isEmpty() || recipeIngredient == null) return;

        this.insertInputsStack(inventory, availableInv, blockEntity, recipeIngredient);

        this.pickupAction(entityMaid);
    }

    default void tryExtractItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        ItemStack mealStack = getBeInvMealStack(blockEntity, inventory);
        ItemStack containerInputStack = inventory.getStackInSlot(getContainerStackSlot());

        ItemStack outputStack = inventory.getStackInSlot(getOutputSlot());
        ItemStack container = getFoodContainer(blockEntity);

        ItemStack outputAdditionItem = maidRecipesManager.findOutputAdditionItem(container);

        // 取出杯具（相当于盛饭需要碗，但是此时你手上有被子；所以需要先取出杯子，再把碗放到你手上）
        if (!mealStack.isEmpty() && !outputAdditionItem.isEmpty()) {
            // 取出杯具
            if (!containerInputStack.isEmpty()) {
                inventory.extractItem(getContainerStackSlot(), containerInputStack.getCount(), false);
                ItemHandlerHelper.insertItemStacked(maidRecipesManager.getOutputAdditionInv(), containerInputStack.copy(), false);
                blockEntity.setChanged();
            }

            // 放入杯具
            inventory.insertItem(getContainerStackSlot(), outputAdditionItem.copy(), false);
            blockEntity.setChanged();
        }


        // 取出最终物品
        extractOutputStack(inventory, maidRecipesManager.getOutputInv(), blockEntity);


        boolean heated = isHeated(blockEntity);
        Optional<R> recipe = getMatchingRecipe(blockEntity, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(blockEntity, recipe.get());
        if (!b && hasInput(inventory)) {
            extractInputsStack(inventory, maidRecipesManager.getInputInv(), blockEntity);
        }


        //当厨锅没有物品，又有杯具在时，就取出杯具
        if (!hasInput(inventory) && !containerInputStack.isEmpty()) {
            inventory.extractItem(getContainerStackSlot(), containerInputStack.getCount(), false);
            ItemHandlerHelper.insertItemStacked(availableInv, containerInputStack.copy(), false);
            blockEntity.setChanged();
        }


        pickupAction(entityMaid);
    }

    @Override
    @SuppressWarnings("unchecked")
    default Optional<R> getMatchingRecipe(B be, RecipeWrapper recipeWrapper) {
        return ((IFdCbeAccessor<R>) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    default boolean canCook(B be, R recipe) {
        return ((IFdCbeAccessor<R>) be).canCook$tlma(recipe);
    }
}
