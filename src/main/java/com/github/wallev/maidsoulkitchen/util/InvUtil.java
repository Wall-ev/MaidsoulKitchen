package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public class InvUtil {

    public static ItemStack getStack(IItemHandler inv, Predicate<ItemStack> predicate) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (predicate.test(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getStack(IItemHandler inv, ItemStack itemStack) {
        return getStack(inv, itemStack.getItem());
    }

    public static ItemStack getStack(IItemHandler inv, Item item) {
        return getStack(inv, stack -> stack.is(item));
    }

    public static boolean hasStack(IItemHandler inv, Predicate<ItemStack> predicate) {
        return !getStack(inv, predicate).isEmpty();
    }

    public static boolean hasStack(IItemHandler inv, ItemStack itemStack) {
        return !getStack(inv, itemStack.getItem()).isEmpty();
    }

    public static boolean hasStack(IItemHandler inv, Item item) {
        return !getStack(inv, item).isEmpty();
    }
}
