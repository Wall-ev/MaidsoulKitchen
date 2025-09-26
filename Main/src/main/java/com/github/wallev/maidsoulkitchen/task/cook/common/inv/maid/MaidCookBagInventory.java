package com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid;

import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaidCookBagInventory extends IMaidCookInventory {
    private final ItemStack stack;
    private final Map<Item, Integer> inventoryItem = new HashMap<>();
    private final Map<Item, List<ItemStack>> inventoryStack = new HashMap<>();
    private final List<ItemStack> lastInvStack = new ArrayList<>();
    private Map<BagType, ItemStackHandler> containers;
    private Map<BagType, IItemHandlerModifiable> itemStackHandlers;

    private IItemHandlerModifiable inputInv;
    private IItemHandlerModifiable outputInv;

    public MaidCookBagInventory(EntityMaid maid, ItemStack stack) {
        super(maid);
        this.stack = stack;
        this.initInvData();
    }

    @Override
    protected void initInvData() {
        this.containers = ItemCulinaryHub.getContainers(stack);


        int i = 0;
        ItemStackHandler[] handlers = new ItemStackHandler[ItemCulinaryHub.INPUT_BAG_TYPES.length];
        for (BagType inputBagType : ItemCulinaryHub.INPUT_BAG_TYPES) {
            handlers[i++] = containers.get(inputBagType);
        }
        CombinedInvWrapper inputInv = new CombinedInvWrapper(handlers);

//        CombinedInvWrapper inputInv = ItemCulinaryHub.getInputInv(stack);
        Map<BagType, IItemHandlerModifiable> itemStackHandlers = new HashMap<>();
        for (BagType inputBagType : ItemCulinaryHub.INPUT_BAG_TYPES) {
            itemStackHandlers.put(inputBagType, inputInv);
        }
        ItemStackHandler outputInv = containers.get(BagType.OUTPUT);
//        ItemStackHandler outputInv = ItemCulinaryHub.getOutputInv(stack);
        itemStackHandlers.put(BagType.OUTPUT, outputInv);
        itemStackHandlers.put(BagType.INPUT, inputInv);
        this.itemStackHandlers = itemStackHandlers;

        this.inputInv = inputInv;
        this.outputInv = outputInv;
    }

    @Override
    public void refreshInv() {
        clearCacheStackInfo();
        this.initInvData();
        IItemHandlerModifiable availableInv = itemStackHandlers.get(BagType.INGREDIENT);
        for (int i = 0; i < availableInv.getSlots(); i++) {
            ItemStack stack = availableInv.getStackInSlot(i);
            proseLastInvStack(i, stack);
            if (stack.isEmpty()) continue;
            add(stack);
            itemInventory.add(stack);
        }
//        MemoryUtil.rememberHubInputInventory(maid, this.itemInventory);
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

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public List<ItemStack> getLastInvStack() {
        return lastInvStack;
    }

    @Override
    public IItemHandlerModifiable getAvailableInv(BagType bagType) {
        return itemStackHandlers.get(bagType);
    }

    @Override
    public IItemHandlerModifiable getInputInv() {
        return this.inputInv;
    }

    @Override
    public IItemHandlerModifiable getOutputInv() {
        return this.outputInv;
    }

    @Override
    public void syncInv() {
        this.calcAvailableSlots();
        ItemCulinaryHub.setContainer(stack, containers);
    }
}
