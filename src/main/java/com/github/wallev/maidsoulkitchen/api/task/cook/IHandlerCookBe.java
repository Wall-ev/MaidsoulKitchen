package com.github.wallev.maidsoulkitchen.api.task.cook;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public interface IHandlerCookBe<B extends BlockEntity> {
    ItemStackHandler getItemStackHandler(B be);
}
