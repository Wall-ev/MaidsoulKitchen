package com.github.wallev.maidsoulkitchen.task.cook.common.inv.chest;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public record ChestInvsData(List<BlockPos> chestPoses, List<BlockEntity> chestBes, List<IItemHandler> chestItemHandlers, int invSlots) {
}
