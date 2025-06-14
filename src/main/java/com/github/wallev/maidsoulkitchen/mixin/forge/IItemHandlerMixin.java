package com.github.wallev.maidsoulkitchen.mixin.forge;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = IItemHandler.class, remap = false)
public interface IItemHandlerMixin extends IInvHandler {

    @Shadow
    int getSlots();

    @Shadow
    ItemStack getStackInSlot(int slot);

    @Shadow
    ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

    @Shadow
    ItemStack extractItem(int slot, int amount, boolean simulate);

    @Override
    default int kl$getSlots() {
        return this.getSlots();
    }

    @Override
    default ItemStack kl$getStackInSlot(int slot) {
        return this.getStackInSlot(slot);
    }

    @Override
    default ItemStack kl$insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.insertItem(slot, stack, simulate);
    }

    @Override
    default ItemStack kl$extractItem(int slot, int amount, boolean simulate) {
        return this.extractItem(slot, amount, simulate);
    }
}
