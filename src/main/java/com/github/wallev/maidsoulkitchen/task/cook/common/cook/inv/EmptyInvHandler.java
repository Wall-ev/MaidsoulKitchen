package com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv;

import net.minecraft.world.item.ItemStack;

public class EmptyInvHandler implements IInvHandler {

    @Override
    public int kl$getSlots() {
        return 0;
    }

    @Override
    public ItemStack kl$getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack kl$insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public ItemStack kl$extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean kl$canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean kl$canTakeItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public int kl$getSlotLimit(int slot) {
        return 0;
    }

}
