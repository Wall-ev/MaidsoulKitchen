package com.github.wallev.maidsoulkitchen.api.task.cook;

import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IContainerCookBe<B extends BlockEntity> {
    Container getContainer(B be);
}
