package com.github.catbert.tlma.task.cook.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaidInventory {
    private final EntityMaid maid;
    private final Map<Item, Integer> inventoryItem = new HashMap<>();
    private final Map<Item, List<ItemStack>> inventoryStack = new HashMap<>();
    private final List<ItemStack> lastInvStack = new ArrayList<>();

    public MaidInventory(EntityMaid maid) {
        this.maid = maid;
        this.refreshInv();
    }

    public void refreshInv() {
        clearCacheStackInfo();
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        List<Integer> blackSlots = getBlackSlots();
        for (int i = 0; i < availableInv.getSlots(); i++) {
            ItemStack stack = availableInv.getStackInSlot(i);
            proseLastInvStack(i, stack);
            if (blackSlots.contains(i)) continue;
            if (stack.isEmpty()) continue;
            add(stack);
        }
    }

    private void proseLastInvStack(int index, ItemStack invStack) {
        if (index < lastInvStack.size()) {
            ItemStack cacheStack = lastInvStack.get(index);
            if (cacheStack.is(invStack.getItem()) && cacheStack != invStack) {
                cacheStack.setCount(invStack.getCount());
                return;
            }
        }
        lastInvStack.add(invStack.copy());
    }

    private void clearCacheStackInfo() {
        inventoryItem.clear();
        inventoryStack.clear();
        lastInvStack.clear();
    }

    private List<Integer> getBlackSlots() {
        List<Integer> blockSlots = new ArrayList<>();
//        BaubleItemHandler maidBauble = this.maid.getMaidBauble();
//        for (int i = 0; i < maidBauble.getSlots(); i++) {
//            if (maidBauble.getStackInSlot(i).getItem() instanceof ItemWirelessIO itemWirelessIO) {
////                itemWirelessIO.get
//            }
//        }
        return blockSlots;
    }

    private void add(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (this.inventoryStack.get(item) == null) {
                List<ItemStack> stackList = new ArrayList<>();
                stackList.add(stack);
                this.inventoryStack.put(item, stackList);
            } else {
                this.inventoryStack.get(item).add(stack);
            }

            this.inventoryItem.merge(item, stack.getCount(), (a, b) -> a + b);
        }
    }

    public Map<Item, List<ItemStack>> getInventoryStack() {
        return inventoryStack;
    }

    public Map<Item, Integer> getInventoryItem() {
        return inventoryItem;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public List<ItemStack> getLastInvStack() {
        return lastInvStack;
    }
}
