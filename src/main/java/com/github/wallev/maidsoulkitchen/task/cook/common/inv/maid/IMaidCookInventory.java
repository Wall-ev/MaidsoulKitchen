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
}
