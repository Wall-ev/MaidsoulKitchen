package com.github.catbert.tlma.api.task.v1.cook;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public interface IHandlerCookBe<B extends BlockEntity> {
    ItemStackHandler getItemStackHandler(B be);
}
