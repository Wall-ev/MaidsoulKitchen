package com.github.wallev.maidsoulkitchen.compat.msm.common.util;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntUnaryOperator;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class SlotLimitInvWrapper implements IItemHandlerModifiable {
    protected final Container inv;
//    @Nullable
//    protected final Direction side;

    private final IntUnaryOperator slotLimit;
    private final InsertLimit newStackInsertLimit;

    private final int[] limitSlots;

    public SlotLimitInvWrapper(Container inv, int... limitSlots) {
        this.inv = inv;
        this.limitSlots = limitSlots;
//        this.side = side;

        // A few special cases to account for canPlaceItem implementations attempting to limit specific inputs to 1,
        // by returning false if there's already a contained item. This doesn't work with modded inserted sizes > 1.
        // - Limit buckets to 1 in furnace fuel inputs.
        // - Limit brewing stand "bottle" inputs to 1.
        // Done using lambdas to avoid the overhead of instanceof checks in hot code.
//        if (inv instanceof BrewingStandBlockEntity)
//            this.slotLimit = wrapperSlot -> getSlot(inv, wrapperSlot, side) < 3 ? 1 : inv.getMaxStackSize();
//        else
            this.slotLimit = wrapperSlot -> inv.getMaxStackSize();
        if (inv instanceof AbstractFurnaceBlockEntity)
            this.newStackInsertLimit = (wrapperSlot, invSlot, stack) -> invSlot == 1 && stack.is(Items.BUCKET) ? 1 : Math.min(stack.getMaxStackSize(), getSlotLimit(wrapperSlot));
        else
            this.newStackInsertLimit = (wrapperSlot, invSlot, stack) -> Math.min(stack.getMaxStackSize(), getSlotLimit(wrapperSlot));
    }

//    @SuppressWarnings("unchecked")
//    public static LazyOptional<IItemHandlerModifiable>[] create(WorldlyContainer inv, Direction... sides) {
//        LazyOptional<IItemHandlerModifiable>[] ret = new LazyOptional[sides.length];
//        for (int x = 0; x < sides.length; x++) {
//            final Direction side = sides[x];
//            ret[x] = LazyOptional.of(() -> new SidedInvWrapper(inv, side));
//        }
//        return ret;
//    }

    public static int getSlot(WorldlyContainer inv, int slot, @Nullable Direction side) {
        int[] slots = inv.getSlotsForFace(side);
        if (slot < slots.length)
            return slots[slot];
        return -1;
    }

    public int getSlot(Container inv, int slot) {
        for (int i : limitSlots) {
            if (i == slot)
                return slot;
        }

        return -1;

//        int[] slots = inv.getSlotsForFace(side);
//        if (slot < slots.length)
//            return slots[slot];
//        return -1;
    }

    public int[] getSlotsForFace() {
//    public int[] getSlotsForFace(Direction pSide) {
        return limitSlots;
    }

    /**
     * Returns {@code true} if automation can insert the given item in the given slot from the given side.
     */
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack) {
//    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @javax.annotation.Nullable Direction pDirection) {
        int slot = getSlot(inv, pIndex);
        if (slot == -1)
            return false;
        return inv.canPlaceItem(slot, pItemStack);
    }

    /**
     * Returns {@code true} if automation can extract the given item in the given slot from the given side.
     */
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack) {
//    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        int slot = getSlot(inv, pIndex);
        if (slot == -1)
            return false;
        return inv.canTakeItem(inv, slot, pStack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SlotLimitInvWrapper that = (SlotLimitInvWrapper) o;

        return inv.equals(that.inv);
//        return inv.equals(that.inv) && side == that.side;
    }

    @Override
    public int hashCode() {
        int result = inv.hashCode();
        result = 31 * result;
//        result = 31 * result + (side == null ? 0 : side.hashCode());
        return result;
    }

    @Override
    public int getSlots() {
        return inv.getContainerSize();
//        return limitSlots.length;
//        return getSlotsForFace().length;
//        return inv.getSlotsForFace(side).length;
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        int i = getSlot(inv, slot);
//        int i = getSlot(inv, slot, side);
        return i == -1 ? ItemStack.EMPTY : inv.getItem(i);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        int slot1 = getSlot(inv, slot);
//        int slot1 = getSlot(inv, slot, side);

        if (slot1 == -1)
            return stack;

        ItemStack stackInSlot = inv.getItem(slot1);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot)))
                return stack;

            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
                return stack;

            if (!this.canPlaceItemThroughFace(slot1, stack) || !inv.canPlaceItem(slot1, stack))
//            if (!inv.canPlaceItemThroughFace(slot1, stack, side) || !inv.canPlaceItem(slot1, stack))
                return stack;

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    setInventorySlotContents(slot1, copy);
                }

                return ItemStack.EMPTY;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    setInventorySlotContents(slot1, copy);
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {
            if (!this.canPlaceItemThroughFace(slot1, stack) || !inv.canPlaceItem(slot1, stack))
//            if (!inv.canPlaceItemThroughFace(slot1, stack, side) || !inv.canPlaceItem(slot1, stack))
                return stack;

            m = newStackInsertLimit.limitInsert(slot, slot1, stack);

            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    setInventorySlotContents(slot1, stack.split(m));
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate)
                    setInventorySlotContents(slot1, stack);
                return ItemStack.EMPTY;
            }
        }

    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        int slot1 = getSlot(inv, slot);
//        int slot1 = getSlot(inv, slot, side);

        if (slot1 != -1)
            setInventorySlotContents(slot1, stack);
    }

    private void setInventorySlotContents(int slot, ItemStack stack) {
        inv.setChanged(); //Notify vanilla of updates, We change the handler to be responsible for this instead of the caller. So mimic vanilla behavior
        inv.setItem(slot, stack);
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        int slot1 = getSlot(inv, slot);
//        int slot1 = getSlot(inv, slot, side);

        if (slot1 == -1)
            return ItemStack.EMPTY;

        ItemStack stackInSlot = inv.getItem(slot1);

        if (stackInSlot.isEmpty())
            return ItemStack.EMPTY;

        if (!this.canTakeItemThroughFace(slot1, stackInSlot))
//        if (!inv.canTakeItemThroughFace(slot1, stackInSlot, side))
            return ItemStack.EMPTY;

        if (simulate) {
            if (stackInSlot.getCount() < amount) {
                return stackInSlot.copy();
            } else {
                ItemStack copy = stackInSlot.copy();
                copy.setCount(amount);
                return copy;
            }
        } else {
            int m = Math.min(stackInSlot.getCount(), amount);
            ItemStack ret = inv.removeItem(slot1, m);
            inv.setChanged();
            return ret;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit.applyAsInt(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        int slot1 = getSlot(inv, slot);
//        int slot1 = getSlot(inv, slot, side);
        return slot1 == -1 ? false : inv.canPlaceItem(slot1, stack);
    }

    private interface InsertLimit {
        int limitInsert(int wrapperSlot, int invSlot, ItemStack stack);
    }
}
