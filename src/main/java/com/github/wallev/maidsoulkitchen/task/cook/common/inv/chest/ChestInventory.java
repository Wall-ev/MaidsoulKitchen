package com.github.wallev.maidsoulkitchen.task.cook.common.inv.chest;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.*;
import java.util.stream.Collectors;

public class ChestInventory {
    public static final int TICK_SCAN_LIMIT = 10;

    private final List<IItemHandler> chestItemHandlers = new ArrayList<>();

    private final Map<ItemDefinition, ChestItemDef> itemDefinitions = new HashMap<>();
    private int lastItemHandlerSlotIndex = 0;
    private int lastItemHandlerIndex = 0;

    public void init(ChestInvsData data) {
        this.initData(data);
    }

    public boolean needReUpdate() {
        return true;
    }

    public boolean tickScan() {
        if (lastItemHandlerIndex >= chestItemHandlers.size()) {
            return true;
        }
        IItemHandler scanItemHandler = chestItemHandlers.get(lastItemHandlerIndex);
        int scanItemHandlerSlotMax = lastItemHandlerSlotIndex + TICK_SCAN_LIMIT;
        boolean thisHandlerEnd = scanItemHandlerSlotMax >= scanItemHandler.getSlots();
        if (thisHandlerEnd) {
            scanItemHandlerSlotMax = scanItemHandler.getSlots();
        }

        for (int i = lastItemHandlerSlotIndex; i < scanItemHandlerSlotMax; i++) {
            lastItemHandlerSlotIndex++;

            ItemStack stackInSlot = scanItemHandler.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                continue;
            }

            ItemDefinition itemDef = ItemDefinition.of(stackInSlot);
            ChestItemDef handlerMap = itemDefinitions.computeIfAbsent(itemDef, k -> new ChestItemDef());
            handlerMap.add(i, scanItemHandler);
        }

        if (thisHandlerEnd) {
            lastItemHandlerIndex++;
            lastItemHandlerSlotIndex = 0;
        }
        return false;
    }

    public void update() {

    }

    protected void reset() {
        chestItemHandlers.clear();
        itemDefinitions.clear();

        this.lastItemHandlerIndex = 0;
        this.lastItemHandlerSlotIndex = 0;
    }

    public void clear() {
        this.reset();
    }

    public boolean done() {
        return lastItemHandlerIndex >= chestItemHandlers.size();
    }

    protected void clearAndInitData(ChestInvsData data) {
        this.clear();
        this.initData(data);
    }

    protected void initData(ChestInvsData data) {
        this.chestItemHandlers.addAll(data.chestItemHandlers());
    }

    public Map<ItemDefinition, ChestItemDef> getItemDefinitions() {
        return itemDefinitions;
    }

    public Map<ItemDefinition, Long> getAvailable() {
        return itemDefinitions.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> (long) entry.getValue().getCount()
        ));
    }

    public static class ChestItemDef {
        private int count;
        private final Map<IItemHandler, List<Integer>> valueMap = new HashMap<>();

        public int getCount() {
            return count;
        }

        public Map<IItemHandler, List<Integer>> getValueMap() {
            return valueMap;
        }

        public void add(int slot, IItemHandler itemHandler) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            count += stackInSlot.getCount();
            valueMap.computeIfAbsent(itemHandler, k -> new ArrayList<>()).add(slot);
        }
    }
}
