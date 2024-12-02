package com.github.wallev.farmsoulkitchen.api.task.v2;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * 用来获取容器处理的...
 */
public interface IBeInv<B extends BlockEntity> {

    int getSlots(B be);

    @NotNull
    ItemStack extractItem(B be, int slot, int amount, boolean simulate);

    void insertItem(B be, int slot, @NotNull ItemStack stack, boolean simulate);

    @NotNull
    ItemStack getStackInSlot(B be, int slot);

    interface IItemHandlerInv<B extends BlockEntity, T extends IItemHandler> extends IBeInv<B> {

        T getItemHandler(B be);

        @Override
        default int getSlots(B be) {
            return getItemHandler(be).getSlots();
        }

        @Override
        @NotNull
        default ItemStack extractItem(B be, int slot, int amount, boolean simulate){
            return getItemHandler(be).extractItem(slot, amount, simulate);
        }

        @Override
        default void insertItem(B be, int slot, @NotNull ItemStack stack, boolean simulate){
            getItemHandler(be).insertItem(slot, stack, simulate);
        }

        @Override
        @NotNull
        default ItemStack getStackInSlot(B be, int slot){
            return getItemHandler(be).getStackInSlot(slot);
        }
    }

    interface IContainerInv<B extends BlockEntity, T extends Container> extends IBeInv<B> {

        T getContainer(B be);

        @Override
        default int getSlots(B be){
            return getContainer(be).getContainerSize();
        }

        @Override
        @NotNull
        default ItemStack extractItem(B be, int slot, int amount, boolean simulate){
            return getContainer(be).removeItem(slot, amount);
        }

        @Override
        default void insertItem(B be, int slot, @NotNull ItemStack stack, boolean simulate){
            getContainer(be).setItem(slot, stack);
        }

        @Override
        @NotNull
        default ItemStack getStackInSlot(B be, int slot){
            return getContainer(be).getItem(slot);
        }
    }

}
