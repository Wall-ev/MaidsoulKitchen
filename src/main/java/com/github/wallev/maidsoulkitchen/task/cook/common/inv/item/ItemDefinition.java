package com.github.wallev.maidsoulkitchen.task.cook.common.inv.item;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemDefinition {
    public static final ItemDefinition EMPTY = new ItemDefinition(ItemStack.EMPTY);

    private final Item item;
    @Nullable
    private final CompoundTag tag;
    private final ItemStack stack;

    protected ItemDefinition(ItemStack stack) {
        this(stack.getItem(), stack.getTag());
    }

    protected ItemDefinition(Item item) {
        this(item, null);
    }

    protected ItemDefinition(Item item, @Nullable CompoundTag tag) {
        this.item = item;
        this.tag = tag;
        this.stack = makeStack();
    }

    public static ItemDefinition of(Item item, @Nullable CompoundTag tag) {
        return new ItemDefinition(item, tag);
    }

    public static ItemDefinition of(ItemStack stack) {
        return new ItemDefinition(stack);
    }

    public static ItemDefinition of(Item item) {
        return new ItemDefinition(item);
    }

    private ItemStack makeStack() {
        return new ItemStack(this.item, 1, this.tag);
    }

    /**
     * 仅仅作用于条件判断，不可用于修改！
     */
    public ItemStack toStack(int count) {
        int c = Math.min(count, stack.getMaxStackSize());
        stack.setCount(c);
        return stack;
    }

    /**
     * 仅仅作用于条件判断，不可用于修改！
     */
    public ItemStack toStack(long count) {
        int c = (int) Math.min(count, stack.getMaxStackSize());
        stack.setCount(c);
        return stack;
    }

    /**
     * 仅仅作用于条件判断，不可用于修改！
     */
    public ItemStack stack() {
        stack.setCount(1);
        return stack;
    }

    public int getMaxStackSize() {
        return stack.getMaxStackSize();
    }

    public boolean isStackable() {
        return stack.isStackable();
    }

    public Item item() {
        return this.item;
    }

    @Nullable
    public CompoundTag tag() {
        return this.tag;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemDefinition that = (ItemDefinition) o;
        return Objects.equals(item, that.item) && Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, tag);
    }

    @Override
    public String toString() {
        return "1 " + item;
    }

    public static class ItemDefinitionMap {
        private static final Object2ObjectOpenHashMap<Item, ItemDefinition> itemMap = new Object2ObjectOpenHashMap<>();
        private static final Object2ObjectOpenHashMap<ItemStack, ItemDefinition> stackMap = new Object2ObjectOpenHashMap<>();

        public ItemDefinition get(Item item) {
            return itemMap.get(item);
        }

    }

}
