package com.github.catbert.tlma.api.task.v1.cook;

import com.github.catbert.tlma.api.IMaidAction;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public interface IItemHandlerCook extends IMaidAction {

    int getOutputSlot();

    default int getInputStartSlot() {
        return 0;
    }

    int getInputSize();

    default void extractOutputStack(ItemStackHandler inventory, CombinedInvWrapper availableInv, BlockEntity blockEntity) {
        ItemStack stackInSlot = inventory.getStackInSlot(this.getOutputSlot());
        ItemStack copy = stackInSlot.copy();

        if (stackInSlot.isEmpty()) return;
        inventory.extractItem(this.getOutputSlot(), stackInSlot.getCount(), false);
        ItemHandlerHelper.insertItemStacked(availableInv, copy, false);
        blockEntity.setChanged();
    }


    default void extractInputStack(ItemStackHandler inventory, CombinedInvWrapper availableInv, BlockEntity blockEntity) {
        for (int i = this.getInputStartSlot(); i < this.getInputSize() + this.getInputStartSlot(); ++i) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            ItemStack copy = stackInSlot.copy();
            if (!stackInSlot.isEmpty()) {
                inventory.extractItem(i, stackInSlot.getCount(), false);
                ItemHandlerHelper.insertItemStacked(availableInv, copy, false);
            }
        }
        blockEntity.setChanged();
    }

    default void insertInputStack(ItemStackHandler inventory, CombinedInvWrapper availableInv, BlockEntity blockEntity, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = getInputStartSlot(), j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                insertAndShrink(inventory, amounts.get(j), ingredients, j, i);
            }
            blockEntity.setChanged();
        }

        updateIngredient(ingredientPair);
    }

    default void updateIngredients(List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients) {

    }

    default void updateIngredient(Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {

    }

    default boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        int i = 0;
        for (List<ItemStack> ingredient : ingredients) {
            int actualCount = amounts.get(i++);
            for (ItemStack itemStack : ingredient) {
                actualCount -= itemStack.getCount();
                if (actualCount <= 0) {
                    break;
                }
            }

            if (actualCount > 0) {
                canInsert = false;
                break;
            }
        }

        return canInsert;
    }

    default void insertAndShrink(ItemStackHandler inventory, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            if(itemStack.isEmpty()) continue;
            int count = itemStack.getCount();

            if (count >= amount) {
                inventory.insertItem(slotIndex, itemStack.copyWithCount(amount), false);
                itemStack.shrink(amount);
                break;
            } else {
                inventory.insertItem(slotIndex, itemStack.copyWithCount(count), false);
                itemStack.shrink(count);
                amount -= count;
                if (amount <= 0) {
                    break;
                }
            }
        }
    }

    default boolean hasInput(ItemStackHandler inventory) {
        for (int i = getInputStartSlot(); i < getInputSize() + getInputStartSlot(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
