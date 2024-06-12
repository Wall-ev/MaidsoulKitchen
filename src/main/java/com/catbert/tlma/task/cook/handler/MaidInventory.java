package com.catbert.tlma.task.cook.handler;

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
    private CombinedInvWrapper lastInv;

    protected MaidInventory(EntityMaid maid) {
        this.maid = maid;
        this.refreshInv();
    }

    public void refreshInv() {
        clearCache();
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        List<Integer> blackSlots = getBlackSlots();
        for (int i = 0; i < availableInv.getSlots(); i++) {
            if (blackSlots.contains(i)) continue;
            ItemStack stack = availableInv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            add(stack);
        }
//        this.lastInv = availableInv;
    }

    private void clearCache() {
        inventoryItem.clear();
        inventoryStack.clear();
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

    public CombinedInvWrapper getLastInv() {
        return lastInv;
    }

}
