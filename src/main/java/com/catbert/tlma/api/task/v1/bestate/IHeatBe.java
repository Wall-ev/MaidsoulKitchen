package com.catbert.tlma.api.task.v1.bestate;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IHeatBe<T extends BlockEntity> {

    boolean isHeated(T be);

}
