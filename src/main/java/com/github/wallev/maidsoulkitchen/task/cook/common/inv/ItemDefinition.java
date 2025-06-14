package com.github.wallev.maidsoulkitchen.task.cook.common.inv;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemDefinition {
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
        this.stack = toStack(this);
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

    private ItemStack toStack(ItemDefinition definition) {
        return new ItemStack(definition.item, 1, tag);
    }

    public ItemStack toStack(int count) {
        int c = Math.min(count, stack.getMaxStackSize());
        stack.setCount(c);
        return stack;
    }

    public ItemStack toStack(long count) {
        int c = (int) Math.min(count, stack.getMaxStackSize());
        stack.setCount(c);
        return stack;
    }

    public ItemStack stack() {
        stack.setCount(1);
        return stack;
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

}
