package com.github.wallev.farmsoulkitchen.task.cook.v1.common.bestate;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IHeatBe<T extends BlockEntity> {

    boolean isHeated(T be);

}
