package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collections;
import java.util.List;

public record MaidRec(Recipe<?> recipe, ItemStack result, int amount, ItemStack oil, ItemStack tool, ItemStack container, List<MaidItem> maidItems, MaidItem fluidItem) {
    public static final MaidRec EMPTY = new MaidRec(null, ItemStack.EMPTY, 0, Collections.emptyList(), MaidItem.EMPTY);

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, ItemStack tool, ItemStack container, List<MaidItem> maidItems, MaidItem fluidItem) {
        this(recipe, result, amount, ItemStack.EMPTY, tool, container, maidItems, fluidItem);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, List<MaidItem> maidItems, MaidItem fluidItem) {
        this(recipe, result, amount, ItemStack.EMPTY, ItemStack.EMPTY, maidItems, fluidItem);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, List<MaidItem> maidItems) {
        this(recipe, result, amount, maidItems, MaidItem.EMPTY);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, ItemStack tool, List<MaidItem> maidItems) {
        this(recipe, result, amount, tool, ItemStack.EMPTY, maidItems, MaidItem.EMPTY);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, ItemStack tool, ItemStack container, List<MaidItem> maidItems) {
        this(recipe, result, amount, tool, container, maidItems, MaidItem.EMPTY);
    }

    @SuppressWarnings("unchecked")
    public <R extends Recipe<? extends Container>> R recCast() {
        return (R) this.recipe();
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
