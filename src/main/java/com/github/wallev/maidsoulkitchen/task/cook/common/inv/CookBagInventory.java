package com.github.wallev.maidsoulkitchen.task.cook.common.inv;

import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookBagInventory extends ICookInventory {
    private final ItemStack stack;
    private final Map<Item, Integer> inventoryItem = new HashMap<>();
    private final Map<Item, List<ItemStack>> inventoryStack = new HashMap<>();
    private final List<ItemStack> lastInvStack = new ArrayList<>();
    private Map<BagType, ItemStackHandler> containers;

    public CookBagInventory(EntityMaid maid, ItemStack stack) {
        super(maid);
        this.stack = stack;
        this.initHubContainerData();
    }

    private void initHubContainerData() {
        containers = ItemCulinaryHub.getContainers(stack);
    }

    @Override
    public void refreshInv() {
        clearCacheStackInfo();
        this.initHubContainerData();
        ItemStackHandler availableInv = containers.getOrDefault(BagType.INGREDIENT, new ItemStackHandler(BagType.INGREDIENT.size * 9));
        List<Integer> blackSlots = getBlackSlots();
        for (int i = 0; i < availableInv.getSlots(); i++) {
            ItemStack stack = availableInv.getStackInSlot(i);
            proseLastInvStack(i, stack);
            if (blackSlots.contains(i)) continue;
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
    protected List<Integer> getBlackSlots() {
        List<Integer> blockSlots = new ArrayList<>();
        return blockSlots;
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

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public List<ItemStack> getLastInvStack() {
        return lastInvStack;
    }

    @Override
    public IItemHandlerModifiable getAvailableInv(BagType bagType) {
        return containers.get(bagType);
    }

    @Override
    public void syncInv() {
        ItemCulinaryHub.setContainer(stack, containers);
    }
}
