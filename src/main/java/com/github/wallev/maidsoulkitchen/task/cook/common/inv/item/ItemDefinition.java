package com.github.wallev.maidsoulkitchen.task.cook.common.inv.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class ItemDefinition {
    public static final ItemDefinition EMPTY = new ItemDefinition(ItemStack.EMPTY);
    public static final Codec<ItemDefinition> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(ItemDefinition::item),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(o -> {
                return Optional.ofNullable(o.tag);
            })
    ).apply(ins, (item, tag) -> {
        return new ItemDefinition(item, tag.orElse(null));
    }));

    private final Item item;
    private final boolean testTag;
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
        this.stack = makeStack();
        this.tag = tag;
        if (this.stack.isDamageableItem()) {
            this.testTag = false;
        } else {
            this.testTag = tag != null;
        }
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
        ItemStack copy = new ItemStack(this.item, 1);
        if (this.tag != null) {
            copy.setTag(this.tag);
        }
        return copy;
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

    public boolean is(ItemDefinition itemDef) {
        if (itemDef.testTag) {
            return this.item.equals(itemDef.item) && Objects.equals(this.tag, itemDef.tag);
        } else {
            return this.item.equals(itemDef.item);
        }
    }

    public boolean is(ItemStack itemStack) {
        if (this.testTag) {
            return item.equals(itemStack.getItem()) && Objects.equals(tag, itemStack.getTag());
        } else {
            return item.equals(itemStack.getItem());
        }
    }

    public boolean is(Item item) {
        return this.item.equals(item);
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
        if (o != null) {
            if (o instanceof ItemDefinition that) {
                return is(that);
            } else if (o instanceof ItemStack that) {
                return is(that);
            } else if (o instanceof Item that) {
                return is(that);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.testTag ? Objects.hash(item, tag) : item.hashCode();
    }

    @Override
    public String toString() {
        return item.toString();
    }

}
