package com.github.wallev.maidsoulkitchen.task.cook.common.inv.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ItemInventory {
    private final Map<Item, Long> items = new HashMap<>();
    private final Map<ItemDefinition, Long> stacks = new HashMap<>();

    private final Map<Item, LinkedList<ItemStack>> itemsMap = new HashMap<>();
    private final Map<ItemDefinition, LinkedList<ItemStack>> stacksMap = new HashMap<>();

    private boolean dirty = false;

//    private final Object2IntLinkedOpenHashMap<Item> items = new Object2IntLinkedOpenHashMap<>();
//    private final Object2IntLinkedOpenHashMap<ItemDefinition> stacks = new Object2IntLinkedOpenHashMap<>();
//    private final Object2ObjectOpenHashMap<Item, LinkedList<ItemStack>> itemsMap = new Object2ObjectOpenHashMap<>();
//    private final Object2ObjectOpenHashMap<ItemDefinition, LinkedList<ItemStack>> stacksMap = new Object2ObjectOpenHashMap<>();

    public void add(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        Item item = stack.getItem();
        long count = stack.getCount();
        ItemDefinition itemDefinition = ItemDefinition.of(stack);

        items.merge(item, count, Long::sum);
        stacks.merge(itemDefinition, count, Long::sum);
        itemsMap.computeIfAbsent(item, k -> new LinkedList<>()).add(stack);
        stacksMap.computeIfAbsent(itemDefinition, k -> new LinkedList<>()).add(stack);
    }

    public void update() {
        if (dirty) {
            itemsMap.values().forEach(list -> {
                for (ItemStack itemStack : list) {
                    if (itemStack == null || itemStack.isEmpty()) {
                        list.remove();
                    } else {
                        break;
                    }
                }
            });

            stacksMap.values().forEach(list -> {
                for (ItemStack itemStack : list) {
                    if (itemStack == null || itemStack.isEmpty()) {
                        list.remove();
                    } else {
                        break;
                    }
                }
            });

            dirty = false;
        }
    }

    public long getItemCount(Item item) {
        return items.get(item);
    }

    public long getItemCount(ItemStack itemStack) {
        return stacks.get(ItemDefinition.of(itemStack));
    }

    public LinkedList<ItemStack> getItemStacks(Item item) {
        return itemsMap.get(item);
    }

    public LinkedList<ItemStack> getItemStacks(ItemDefinition definition) {
        return stacksMap.get(definition);
    }

    public LinkedList<ItemStack> getItemStacks(ItemStack itemStack) {
        return getItemStacks(itemStack.getItem());
    }

    public LinkedList<ItemStack> getItemStacksWithNbt(ItemStack itemStack) {
        return getItemStacks(ItemDefinition.of(itemStack));
    }

    public Map<Item, Long> getItems() {
        return items;
    }

    public Map<ItemDefinition, Long> getStacks() {
        return stacks;
    }

    public Map<Item, LinkedList<ItemStack>> getItemsMap() {
        return itemsMap;
    }

    public Map<ItemDefinition, LinkedList<ItemStack>> getStacksMap() {
        return stacksMap;
    }

    public void markDirty() {
        dirty = true;
    }

    public void clear() {
        dirty = false;

        items.clear();
        stacks.clear();
        itemsMap.clear();
        stacksMap.clear();
    }
}
