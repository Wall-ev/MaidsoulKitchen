package com.github.wallev.maidsoulkitchen.compat.msm.common.util;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class WrappedPredicateHandler implements IItemHandlerModifiable {

    private final IItemHandlerModifiable handler;
    private final Predicate<Integer> extract;
    private final BiPredicate<Integer, ItemStack> insert;

    public WrappedPredicateHandler(IItemHandlerModifiable handler, Predicate<Integer> extract, BiPredicate<Integer, ItemStack> insert) {
        this.handler = handler;
        this.extract = extract;
        this.insert = insert;
    }

    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.handler.setStackInSlot(slot, stack);
    }

    public int getSlots() {
        return this.handler.getSlots();
    }

    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return this.handler.getStackInSlot(slot);
    }

    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.insert.test(slot, stack) ? this.handler.insertItem(slot, stack, simulate) : stack;
    }

    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.extract.test(slot) ? this.handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    public int getSlotLimit(int slot) {
        return this.handler.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.insert.test(slot, stack) && this.handler.isItemValid(slot, stack);
    }
}