package com.github.wallev.farmsoulkitchen.chest;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

public class RandomizableCbeType implements IChestType {
    @Override
    public boolean isChest(BlockEntity blockEntity) {
        return blockEntity instanceof RandomizableContainerBlockEntity;
    }

    @Override
    public boolean canOpenByPlayer(BlockEntity blockEntity, Player player) {
        return blockEntity instanceof RandomizableContainerBlockEntity cbe && cbe.canOpen(player);
    }

    @Override
    public int getOpenCount(BlockGetter blockGetter, BlockPos blockPos, BlockEntity blockEntity) {
        return 0;
    }
}
