package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class MaidDataUtil {

    public static final EntityDataAccessor<Integer> START_Y_OFFSET = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);

    public static void setStartYOffset(EntityMaid maid, int offset) {
        maid.getEntityData().set(START_Y_OFFSET, offset);
    }

    public static ItemStack getMaidSlotStack(EntityMaid maid, Integer slot) {
        return maid.getAvailableInv(true).getStackInSlot(slot);
    }

    public static Integer getStartYOffset(EntityMaid maid) {
        return maid.getEntityData().get(START_Y_OFFSET);
    }

    public static Integer getMaidInventoryItemStackSlot(EntityMaid maid, Predicate<ItemStack> predicate) {
        Map<Integer, ItemStack> map = new HashMap<>();
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                if (predicate.test(slotStack)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Integer findMaidInventoryItemStack(EntityMaid maid, Predicate<ItemStack> predicate) {
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                if (predicate.test(slotStack)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Integer findMaidInventoryItemStack(CombinedInvWrapper availableInv, Predicate<ItemStack> predicate) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty() && predicate.test(slotStack)) return i;
        }
        return -1;
    }

    public static ItemStack findMaidInventoryStack(CombinedInvWrapper availableInv, Predicate<ItemStack> predicate) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty() && predicate.test(slotStack)) return slotStack;
        }
        return ItemStack.EMPTY;
    }

    public static void consumerMaidInv(CombinedInvWrapper availableInv, Consumer<ItemStack> printConsumer) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                printConsumer.accept(slotStack);
            }
        }
    }

    public static void consumerPairMaidInv(CombinedInvWrapper availableInv, Consumer<Pair<ItemStack, Integer>> printConsumer) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                printConsumer.accept(new Pair<>(slotStack, i));
            }
        }
    }

    public static Integer findInventoryItemStack(IItemHandler chestInv, Predicate<ItemStack> predicate) {
        for (int i = 0; i < chestInv.getSlots(); ++i) {
            ItemStack slotStack = chestInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                if (predicate.test(slotStack)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Map<Integer, ItemStack> getMaidInventoryItemStacks(EntityMaid maid) {
        Map<Integer, ItemStack> map = new HashMap<>();
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                map.put(i, slotStack);
            }
        }
        return map;
    }
}
