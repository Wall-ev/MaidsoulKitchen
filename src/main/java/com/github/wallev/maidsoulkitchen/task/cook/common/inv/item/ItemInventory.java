package com.github.wallev.maidsoulkitchen.task.cook.common.inv.item;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ItemInventory {

    private final Map<Item, Long> items;
    private final Map<ItemDefinition, Long> stacks;
    private final Map<Item, LinkedList<ItemStack>> itemsMap;
    private final Map<ItemDefinition, LinkedList<ItemStack>> stacksMap;
    private boolean dirty = false;

    public ItemInventory() {
        this.items = new HashMap<>();
        this.stacks = new HashMap<>();
        this.itemsMap = new HashMap<>();
        this.stacksMap = new HashMap<>();
        this.dirty = false;
    }

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
            int b = 0;

            stacksMap.values().forEach(list -> {
                IntArrayList removeList = new IntArrayList();

                int i = 0;
                for (ItemStack itemStack : list) {
                    if (itemStack == null || itemStack.isEmpty()) {
                        removeList.add(i);
                    }
                    i++;
                }

                int lastRemoveIndex = 0;
                for (int j : removeList) {
                    list.remove(j - lastRemoveIndex++);
                }
                int a = 1;
            });


            int a = 1;


            itemsMap.values().forEach(list -> {
                IntArrayList removeList = new IntArrayList();

                int i = 0;
                for (ItemStack itemStack : list) {
                    if (itemStack == null || itemStack.isEmpty()) {
                        removeList.add(i);
                    }
                    i++;
                }

                int lastRemoveIndex = 0;
                for (int j : removeList) {
                    list.remove(j - lastRemoveIndex++);
                }
                int c = 1;
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
