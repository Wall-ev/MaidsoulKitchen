package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public final class BlockUtil {
    private BlockUtil(){

    }

    public static String getId(Block block) {
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
        if (key != null) {
            return key.toString();
        }
        return "";
    }

    public static String getId(BlockState blockState) {
        return getId(blockState.getBlock());
    }
}
