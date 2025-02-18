package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class ContainerMaidCookBe<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractMaidCookBe<B, R> implements IInvMcb.IContainerMcb {
    public ContainerMaidCookBe(EntityMaid maid, MaidRecipesManager<?, B, R> recipesManager) {
        super(maid, recipesManager);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.getCookBeInv().removeItem(slot, amount);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        this.getCookBeInv().setItem(slot, stack);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.getCookBeInv().getItem(slot);
    }

}
