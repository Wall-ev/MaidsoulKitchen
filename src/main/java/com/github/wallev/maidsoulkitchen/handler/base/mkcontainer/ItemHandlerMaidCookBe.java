package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class ItemHandlerMaidCookBe<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractMaidCookBe<B, R> implements IInvMcb.IItemHandlerMcb {
    public ItemHandlerMaidCookBe(EntityMaid maid, MaidRecipesManager<R> recipesManager, B cookBe) {
        super(maid, recipesManager, cookBe);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.getCookBeInv().extractItem(slot, amount, simulate);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.getCookBeInv().insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.getCookBeInv().getStackInSlot(slot);
    }

}
