package com.github.wallev.maidsoulkitchen.handler.base.container;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.IOutputAddition;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.MkContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TakeOutputContainerSerializer<MCB extends AbstractMaidCookBe<B, R> & IOutputAddition.NeedOutputContainer,
        B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookBlockEntitySerializer<MCB, B, R> {
    @Override
    public boolean canDoMaidCookBe(MCB maidCookBe) {
        return !MkContainerHelper.hasItemInSlot(maidCookBe, maidCookBe.getOutputContainerSlot());
    }

    @Override
    public void doMaidCookBe(MCB maidCookBe) {
        MkContainerHelper.extractStackInSlot(maidCookBe, maidCookBe.getOutputContainerSlot());
    }
}
