package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileUtil {

    public static void makeChanged(BlockEntity tile) {
        tile.setChanged();
        Level world = tile.getLevel();
        if (world != null) {
            world.sendBlockUpdated(tile.getBlockPos(), tile.getBlockState(), tile.getBlockState(), Block.UPDATE_ALL);
        }
    }

    public static void makeChanged(BlockPos pos, Level world) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null) {
            tile.setChanged();
            world.sendBlockUpdated(tile.getBlockPos(), tile.getBlockState(), tile.getBlockState(), Block.UPDATE_ALL);
        }
    }

}
