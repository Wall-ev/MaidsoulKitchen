package com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaidInventory extends IMaidCookInventory {
    private final Map<Item, Integer> inventoryItem = new HashMap<>();
    private final Map<Item, List<ItemStack>> inventoryStack = new HashMap<>();
    private final List<ItemStack> lastInvStack = new ArrayList<>();
    private CombinedInvWrapper inv;

    public MaidInventory(EntityMaid maid) {
        super(maid);
        this.initInvData();
    }

    @Override
    protected void initInvData() {
        this.inv = maid.getAvailableInv(true);
    }

    public void refreshInv() {
        clearCacheStackInfo();
        this.initInvData();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            proseLastInvStack(i, stack);
            if (stack.isEmpty()) continue;
            add(stack);
            itemInventory.add(stack);
        }
    }

    @Override
    protected void proseLastInvStack(int index, ItemStack invStack) {
        if (index < lastInvStack.size()) {
            ItemStack cacheStack = lastInvStack.get(index);
            if (cacheStack.is(invStack.getItem()) && cacheStack != invStack) {
                cacheStack.setCount(invStack.getCount());
                return;
            }
        }
        lastInvStack.add(invStack.copy());
    }

    @Override
    protected void clearCacheStackInfo() {
        itemInventory.clear();

        inventoryItem.clear();
        inventoryStack.clear();
        lastInvStack.clear();
    }

    @Override
    protected void add(ItemStack stack) {
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

    @Override
    public Map<Item, List<ItemStack>> getInventoryStack() {
        return inventoryStack;
    }

    @Override
    public Map<Item, Integer> getInventoryItem() {
        return inventoryItem;
    }

    @Override
    public List<ItemStack> getLastInvStack() {
        return lastInvStack;
    }

    @Override
    public IItemHandlerModifiable getAvailableInv(BagType bagType) {
        return inv;
    }

    @Override
    public IItemHandlerModifiable getInputInv() {
        return this.inv;
    }

    @Override
    public IItemHandlerModifiable getOutputInv() {
        return this.inv;
    }
}
