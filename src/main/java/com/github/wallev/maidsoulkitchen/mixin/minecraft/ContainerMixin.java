package com.github.wallev.maidsoulkitchen.mixin.minecraft;

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

    @Override
    default int kl$getSlots() {
        return this.getContainerSize();
    }

    @Override
    default ItemStack kl$getStackInSlot(int slot) {
        return this.getItem(slot);
    }

    @Override
    default ItemStack kl$insertItem(int slot, ItemStack stack, boolean simulate) {
        this.setItem(slot, stack);
        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack kl$extractItem(int slot, int amount, boolean simulate) {
        return this.removeItem(slot, amount);
    }
}
