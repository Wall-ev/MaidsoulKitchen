package com.github.wallev.farmsoulkitchen.api.task.v1.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.action.IMaidAction;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public interface IItemHandlerCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends IMaidAction {

    int getOutputSlot();
    default int getInputStartSlot() {
        return 0;
    }
    int getInputSize();

    ItemStackHandler getBeInv(B be);
    default ItemStackHandler getBeInputInv(B be) {
        return getBeInv(be);
    }
    default ItemStackHandler getBeOutputInv(B be) {
        return getBeInv(be);
    }
    default IItemHandlerModifiable getIngreInv(MaidRecipesManager<R> manager) {
        return manager.getIngredientInv();
    }
    default IItemHandlerModifiable getIngreInputsInv(MaidRecipesManager<R> manager) {
        return manager.getInputInv();
    }
    default IItemHandlerModifiable getIngreOutputInv(MaidRecipesManager<R> manager) {
        return manager.getOutputInv();
    }


    default void extractOutputStack(EntityMaid maid, B be, MaidRecipesManager<R> manager) {
        this.extractInputsStack(this.getBeOutputInv(be), this.getIngreInputsInv(manager), be);
    }
    default void extractOutputStack(ItemStackHandler beInv, IItemHandlerModifiable ingreOutputInv, B be) {
        ItemStack outputStack = beInv.getStackInSlot(this.getOutputSlot());
        if (outputStack.isEmpty()) return;

        this.beInv2ingreInv(beInv, ingreOutputInv, outputStack, this.getOutputSlot());
        this.makeChange(be);
    }

    default void extractInputsStack(EntityMaid maid, B be, MaidRecipesManager<R> manager) {
        this.extractInputsStack(this.getBeInputInv(be), this.getIngreInputsInv(manager), be);
    }
    default void extractInputsStack(ItemStackHandler beInv, IItemHandlerModifiable ingreInputInv, B be) {
        for (int i = this.getInputStartSlot(); i < this.getInputSize() + this.getInputStartSlot(); ++i) {
            ItemStack inputStack = beInv.getStackInSlot(i);
            if (inputStack.isEmpty()) continue;

            beInv2ingreInv(beInv, ingreInputInv, inputStack, i);
        }
        this.makeChange(be);
    }

    default void beInv2ingreInv(ItemStackHandler beInv, IItemHandlerModifiable ingreInv, ItemStack extractStack, int beSlot) {
        this.beInv2ingreInv(beInv, ingreInv, extractStack.copy(), beSlot, extractStack);
    }
    default void beInv2ingreInv(ItemStackHandler beInv, IItemHandlerModifiable ingreInv, ItemStack copy, int beSlot, ItemStack extractStack) {
        ItemStack insertedStack = ItemHandlerHelper.insertItemStacked(ingreInv, copy, false);
        beInv.extractItem(beSlot, extractStack.getCount() - insertedStack.getCount(), false);
    }

    default void insertInputsStack(ItemStackHandler beInv, IItemHandlerModifiable ingreInputsInv, B be, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = getInputStartSlot(), j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                insertAndShrink(beInv, amounts.get(j), ingredients, j, i);
            }
            this.makeChange(be);
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

    default void insertAndShrink(ItemStackHandler beInv, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            if(itemStack.isEmpty()) continue;
            int count = itemStack.getCount();

            if (count >= amount) {
                ItemStack leftInsertedStack = beInv.insertItem(slotIndex, itemStack.copyWithCount(amount), false);
                itemStack.shrink(amount - leftInsertedStack.getCount());
                break;
            } else {
                ItemStack leftInsertedStack = beInv.insertItem(slotIndex, itemStack.copyWithCount(amount), false);
                itemStack.shrink(amount - leftInsertedStack.getCount());
                amount -= count;
                if (amount <= 0) {
                    break;
                }
            }
        }
    }

    default boolean hasInput(B be) {
        return this.hasInput(getBeInputInv(be));
    }
    default boolean hasInput(ItemStackHandler beInv) {
        for (int i = getInputStartSlot(); i < getInputSize() + getInputStartSlot(); i++) {
            if (!beInv.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    default void makeChange(B be) {
        this.makeBeChange(be);
    }

    default void makeBeChange(B be) {
        be.setChanged();
    }
}
