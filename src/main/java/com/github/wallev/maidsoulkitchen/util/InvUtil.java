package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.function.Predicate;

public class InvUtil {

//    public static CombinedInvWrapper getAvailableInv(EntityMaid maid) {
//        int availableMaxContainerIndex = maid.getMaidBackpackType().getAvailableMaxContainerIndex();
//        return new CombinedInvWrapper(maid.getHandsInvWrapper(), )
//    }

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

    public static void insertAndPop(EntityMaid maid, List<ItemStack> stacks) {
        CombinedInvWrapper inv = maid.getAvailableInv(true);
        for (ItemStack stack : stacks) {
            ItemStack left = ItemHandlerHelper.insertItemStacked(inv, stack, false);
            if (!left.isEmpty()) {
                maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), left));
            }
        }
    }

    public static void insertAndPop(EntityMaid maid, ItemStack... stacks) {
        insertAndPop(maid, List.of(stacks));
    }

    public static void insertAndPop(EntityMaid maid, ItemStack stack) {
        CombinedInvWrapper inv = maid.getAvailableInv(true);
        ItemStack left = ItemHandlerHelper.insertItemStacked(inv, stack, false);
        if (!left.isEmpty()) {
            maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), left));
        }
    }
}
