package com.github.wallev.maidsoulkitchen.mixin.core.minecraft;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Container.class)
public interface ContainerMixin extends IInvHandler {

    @Shadow
    int getContainerSize();

    @Shadow
    ItemStack getItem(int pSlot);

    @Shadow
    void setItem(int pSlot, ItemStack pStack);

    @Shadow
    ItemStack removeItem(int pSlot, int pAmount);

    @Shadow int getMaxStackSize();

    @Shadow boolean canPlaceItem(int pIndex, ItemStack pStack);

    @Shadow void setChanged();

    @Shadow boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack);

    @Override
    default int kl$getSlots() {
        return this.getContainerSize();
    }

    @Override
    default ItemStack kl$getStackInSlot(int slot) {
        return this.getItem(slot);
    }

    @Override
    default int kl$getSlotLimit(int slot) {
        return this.getMaxStackSize();
    }

    @Override
    default boolean kl$canTakeItem(int slot, ItemStack stack) {
        return this.canTakeItem(this.kl$castAny(), slot, stack);
    }

    @Override
    default boolean kl$canPlaceItem(int slot, ItemStack stack) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    default ItemStack kl$insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = this.getItem(slot);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), kl$getSlotLimit(slot))) {
                return stack;
            }

            if (!ItemStack.isSameItemSameTags(stack, stackInSlot)) {
                return stack;
            }

            if (!this.canPlaceItem(slot, stack)) {
                return stack;
            }

            m = Math.min(stack.getMaxStackSize(), kl$getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    this.setItem(slot, copy);
                    this.setChanged();
                }

                return ItemStack.EMPTY;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    this.setItem(slot, copy);
                    this.setChanged();
                } else {
                    stack.shrink(m);
                }
                return stack;
            }
        } else {
            if (!this.canPlaceItem(slot, stack)) {
                return stack;
            }

            m = Math.min(stack.getMaxStackSize(), kl$getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    this.setItem(slot, stack.split(m));
                    this.setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                    this.setItem(slot, stack);
                    this.setChanged();
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    default ItemStack kl$extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = this.getItem(slot);

        if (stackInSlot.isEmpty()) {
            return ItemStack.EMPTY;
        }

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

            ItemStack decrStackSize = this.removeItem(slot, m);
            this.setChanged();
            return decrStackSize;
        }
    }
}
