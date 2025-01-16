package com.github.wallev.maidsoulkitchen.handler.base.container;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.MkContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TakeOutputSerializer<MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookBlockEntitySerializer<MCB, B, R> {
    @Override
    public boolean canDoMaidCookBe(MCB maidCookBe) {
        return this.canTakeOutput(maidCookBe);
    }

    @Override
    public void doMaidCookBe(MCB maidCookBe) {
        this.takeOutput(maidCookBe);
    }

    protected boolean canTakeOutput(MCB maidCookBe) {
        return MkContainerHelper.hasOutput(maidCookBe);
    }

    protected void takeOutput(MCB maidCookBe) {
        MkContainerHelper.extractOutputStack(maidCookBe);
    }

}
