package com.github.wallev.maidsoulkitchen.handler.base.container;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractCookBlockEntitySerializer<MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> {

    public abstract boolean canDoMaidCookBe(MCB maidCookBe);

    public abstract void doMaidCookBe(MCB maidCookBe);
}
