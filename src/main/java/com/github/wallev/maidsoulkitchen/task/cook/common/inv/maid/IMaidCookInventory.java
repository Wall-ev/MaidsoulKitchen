package com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid;

import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.*;

public abstract class IMaidCookInventory {
    protected final EntityMaid maid;
    protected final ItemInventory itemInventory = new ItemInventory();
    protected int inputAvailableSlots = 0;
    protected int outputAvailableSlots = 0;

    protected boolean hasInputAvailableSlot = false;
    protected boolean hasOutputAvailableSlot = false;

    protected IMaidCookInventory(EntityMaid maid) {
        this.maid = maid;
    }

    protected abstract void initInvData();

    public abstract void refreshInv();

    protected abstract void proseLastInvStack(int index, ItemStack invStack);

    protected abstract void clearCacheStackInfo();

    protected abstract void add(ItemStack stack);

    public abstract Map<Item, List<ItemStack>> getInventoryStack();

    public Map<Item, LinkedList<ItemStack>> getInventoryStackQueue() {
        LinkedHashMap<Item, LinkedList<ItemStack>> itemQueueLinkedHashMap = new LinkedHashMap<>();
        getInventoryStack().forEach((item, itemStacks) -> {
            itemQueueLinkedHashMap.computeIfAbsent(item, k -> new LinkedList<>()).addAll(itemStacks);
        });
        return itemQueueLinkedHashMap;
    }

    public abstract Map<Item, Integer> getInventoryItem();

    public abstract List<ItemStack> getLastInvStack();

    public abstract IItemHandlerModifiable getAvailableInv(BagType bagType);

    public abstract IItemHandlerModifiable getInputInv();

    public abstract IItemHandlerModifiable getOutputInv();

    public void syncInv() {
    }

    public ItemInventory getItemInventory() {
        return itemInventory;
    }

    public void calcAvailableSlots() {
        int a = 0;

        IItemHandlerModifiable inputInv = this.getInputInv();
        this.hasInputAvailableSlot = false;
        for (int i = 0; i < inputInv.getSlots(); i++) {
            if (inputInv.getStackInSlot(i).isEmpty()) {
                this.hasInputAvailableSlot = true;
                a++;
//                break;
            }
        }
        this.inputAvailableSlots = a;
        a = 0;

        IItemHandlerModifiable outputInv = this.getOutputInv();
        this.hasOutputAvailableSlot = false;
        for (int i = 0; i < outputInv.getSlots(); i++) {
            if (outputInv.getStackInSlot(i).isEmpty()) {
                this.hasOutputAvailableSlot = true;
                a++;
//                break;
            }
        }
        this.outputAvailableSlots = a;
        a = 0;
    }

    public int getInputAvailableSlots() {
        return inputAvailableSlots;
    }
//
//    public boolean hasInputAvailableSlot() {
//        return this.inputAvailableSlots > 0;
//    }

    public int getOutputAvailableSlots() {
        return outputAvailableSlots;
    }
//
//    public boolean hasOutputAvailableSlot() {
//        return this.outputAvailableSlots > 0;
//    }

    public boolean hasInputAvailableSlot() {
        return hasInputAvailableSlot;
    }

    public boolean hasOutputAvailableSlot() {
        return hasOutputAvailableSlot;
    }
}
