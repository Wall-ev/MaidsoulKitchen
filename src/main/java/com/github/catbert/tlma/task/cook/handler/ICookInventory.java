package com.github.catbert.tlma.task.cook.handler;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public interface ICookInventory {

    void refreshInv();

    void proseLastInvStack(int index, ItemStack invStack);

    void clearCacheStackInfo();

    List<Integer> getBlackSlots();

    void add(ItemStack stack);

    public Map<Item, List<ItemStack>> getInventoryStack();

    public Map<Item, Integer> getInventoryItem();

    List<ItemStack> getLastInvStack();

    default void syncInv() {

    }

}
