package com.github.wallev.maidsoulkitchen.task.cook.common.inv.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemInventory {
    public static final Codec<ItemInventory> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ItemCount.CODEC.listOf().fieldOf("itemsList").forGetter(o -> o.itemsList),
            ItemDefCount.CODEC.listOf().fieldOf("stacksList").forGetter(o -> o.stacksList),
            ItemStack.CODEC.listOf().listOf().fieldOf("itemsMapList").forGetter(o -> o.itemsMapList),
            ItemStack.CODEC.listOf().listOf().fieldOf("stacksMapList").forGetter(o -> o.stacksMapList),
            Codec.BOOL.fieldOf("dirty").forGetter(o -> o.dirty)
    ).apply(ins, ItemInventory::new));

    private final Map<Item, Long> items;
    private final Map<ItemDefinition, Long> stacks;
    private final Map<Item, LinkedList<ItemStack>> itemsMap;
    private final Map<ItemDefinition, LinkedList<ItemStack>> stacksMap;
    private boolean dirty = false;

    private final List<ItemCount> itemsList;
    private final List<ItemDefCount> stacksList;
    private final List<List<ItemStack>> itemsMapList;
    private final List<List<ItemStack>> stacksMapList;

    private ItemInventory(List<ItemCount> itemsList, List<ItemDefCount> stacksList, List<List<ItemStack>> itemsMapList, List<List<ItemStack>> stacksMapList, boolean dirty) {
        this.itemsList = itemsList;
        this.stacksList = stacksList;
        this.itemsMapList = itemsMapList;
        this.stacksMapList = stacksMapList;
        this.dirty = dirty;

        this.items = new HashMap<>();
        itemsList.forEach(itemCount -> {
            items.put(itemCount.item(), itemCount.count());
        });
        this.stacks = new HashMap<>();
        stacksList.forEach(itemDefCount -> {
            stacks.put(itemDefCount.itemDefinition(), itemDefCount.count());
        });
        this.itemsMap = new HashMap<>();
        itemsMapList.forEach(itemStacks -> {
            Item item = itemStacks.get(0).getItem();
            itemsMap.put(item, new LinkedList<>(itemStacks));
        });
        this.stacksMap = new HashMap<>();
        stacksMapList.forEach(itemStacks -> {
            ItemDefinition itemDefinition = ItemDefinition.of(itemStacks.get(0));
            stacksMap.put(itemDefinition, new LinkedList<>(itemStacks));
        });
    }

    public ItemInventory() {
        this.items = new HashMap<>();
        this.stacks = new HashMap<>();
        this.itemsMap = new HashMap<>();
        this.stacksMap = new HashMap<>();
        this.itemsList = new LinkedList<>();
        this.stacksList = new LinkedList<>();
        this.itemsMapList = new LinkedList<>();
        this.stacksMapList = new LinkedList<>();
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

        addItemStack2ItemsList(stack);
        addItemStack2StacksList(stack);
        addItemStack2ItemsMapList(stack);
        addItemStack2StacksMapList(stack);
    }

    private void addItemStack2ItemsList(ItemStack stack) {
        for (ItemCount itemCount : itemsList) {
            Item item = itemCount.item();
            if (item == stack.getItem()) {
                itemCount.addCount(stack.getCount());
                return;
            }
        }
        itemsList.add(new ItemCount(stack.getItem(), stack.getCount()));
    }

    private void addItemStack2StacksList(ItemStack stack) {
        for (ItemDefCount itemDefCount : stacksList) {
            ItemDefinition itemDefinition = itemDefCount.itemDefinition();
            if (itemDefinition.equals(ItemDefinition.of(stack))) {
                itemDefCount.addCount(stack.getCount());
                return;
            }
        }
        stacksList.add(new ItemDefCount(ItemDefinition.of(stack), stack.getCount()));
    }

    private void addItemStack2ItemsMapList(ItemStack stack) {
        for (List<ItemStack> itemStacks : itemsMapList) {
            Item item = itemStacks.get(0).getItem();
            if (item == stack.getItem()) {
                itemStacks.add(stack);
                return;
            }
        }
        List<ItemStack> itemStacks = new LinkedList<>();
        itemStacks.add(stack);
        itemsMapList.add(itemStacks);
    }

    private void addItemStack2StacksMapList(ItemStack stack) {
        for (List<ItemStack> itemStacks : stacksMapList) {
            ItemDefinition itemDefinition = ItemDefinition.of(itemStacks.get(0));
            if (itemDefinition.equals(ItemDefinition.of(stack))) {
                itemStacks.add(stack);
                return;
            }
        }
        List<ItemStack> itemStacks = new LinkedList<>();
        itemStacks.add(stack);
        stacksMapList.add(itemStacks);
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

    private static class ItemDefCount {
        public static final Codec<ItemDefCount> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ItemDefinition.CODEC.fieldOf("itemDefinition").forGetter(o -> o.itemDefinition),
                Codec.LONG.fieldOf("count").forGetter(o -> o.count)
        ).apply(ins, ItemDefCount::new));
        private ItemDefinition itemDefinition;
        private long count;

        public ItemDefCount(ItemDefinition itemDefinition, long count) {
            this.itemDefinition = itemDefinition;
            this.count = count;
        }

        public ItemDefinition getItemDefinition() {
            return itemDefinition;
        }

        public void setItemDefinition(ItemDefinition itemDefinition) {
            this.itemDefinition = itemDefinition;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public void addCount(long count) {
            this.count += count;
        }

        public ItemDefinition itemDefinition() {
            return this.itemDefinition;
        }

        public long count() {
            return this.count;
        }
    }

    private static class ItemCount {
        public static final Codec<ItemCount> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(o -> o.item),
                Codec.LONG.fieldOf("count").forGetter(o -> o.count)
        ).apply(ins, ItemCount::new));
        private Item item;
        private long count;

        public ItemCount(Item item, long count) {
            this.item = item;
            this.count = count;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public void addCount(long count) {
            this.count += count;
        }

        public Item item() {
            return this.item;
        }

        public long count() {
            return this.count;
        }
    }
}
