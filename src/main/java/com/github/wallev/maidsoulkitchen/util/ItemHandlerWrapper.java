package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import java.util.List;

public class ItemHandlerWrapper implements IItemHandler {
    protected final List<IItemHandler> itemHandlers;
    protected final int itemHandlerLength;
    protected final int[] baseIndex;
    protected final int slotCount;

    public ItemHandlerWrapper(List<IItemHandler> itemHandlers) {
        this.itemHandlers = itemHandlers;
        this.itemHandlerLength = itemHandlers.size();
        this.baseIndex = new int[itemHandlerLength];
        int index = 0;

        for(int i = 0; i < itemHandlerLength; ++i) {
            index += itemHandlers.get(i).getSlots();
            this.baseIndex[i] = index;
        }

        this.slotCount = index;
    }

    protected int getIndexForSlot(int slot) {
        if (slot < 0) {
            return -1;
        } else {
            for(int i = 0; i < this.baseIndex.length; ++i) {
                if (slot - this.baseIndex[i] < 0) {
                    return i;
                }
            }

            return -1;
        }
    }

    protected IItemHandler getHandlerFromIndex(int index) {
        return index >= 0 && index < itemHandlerLength ? this.itemHandlers.get(index) : EmptyHandler.INSTANCE;
    }

    protected int getSlotFromIndex(int slot, int index) {
        return index > 0 && index < this.baseIndex.length ? slot - this.baseIndex[index - 1] : slot;
    }

    public int getSlots() {
        return this.slotCount;
    }

    public ItemStack getStackInSlot(int slot) {
        int index = this.getIndexForSlot(slot);
        IItemHandler handler = this.getHandlerFromIndex(index);
        slot = this.getSlotFromIndex(slot, index);
        return handler.getStackInSlot(slot);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        int index = this.getIndexForSlot(slot);
        IItemHandler handler = this.getHandlerFromIndex(index);
        slot = this.getSlotFromIndex(slot, index);
        return handler.insertItem(slot, stack, simulate);
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        int index = this.getIndexForSlot(slot);
        IItemHandler handler = this.getHandlerFromIndex(index);
        slot = this.getSlotFromIndex(slot, index);
        return handler.extractItem(slot, amount, simulate);
    }

    public int getSlotLimit(int slot) {
        int index = this.getIndexForSlot(slot);
        IItemHandler handler = this.getHandlerFromIndex(index);
        int localSlot = this.getSlotFromIndex(slot, index);
        return handler.getSlotLimit(localSlot);
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        int index = this.getIndexForSlot(slot);
        IItemHandler handler = this.getHandlerFromIndex(index);
        int localSlot = this.getSlotFromIndex(slot, index);
        return handler.isItemValid(localSlot, stack);
    }
}
