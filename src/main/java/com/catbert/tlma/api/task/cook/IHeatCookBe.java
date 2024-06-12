package com.catbert.tlma.api.task.cook;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IHeatCookBe<T extends BlockEntity> {

    boolean isHeated(T be);

}
