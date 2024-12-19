package com.github.wallev.maidsoulkitchen.chest;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity;

public class FarmDelightCabinet implements IChestType {
    @Override
    public boolean isChest(BlockEntity blockEntity) {
        return Mods.FD.isLoaded() && blockEntity instanceof CabinetBlockEntity;
    }

    @Override
    public boolean canOpenByPlayer(BlockEntity blockEntity, Player player) {
        return Mods.FD.isLoaded() && blockEntity instanceof CabinetBlockEntity cabinetBlockEntity && cabinetBlockEntity.canOpen(player);
    }

    @Override
    public int getOpenCount(BlockGetter blockGetter, BlockPos blockPos, BlockEntity blockEntity) {
        return 0;
    }
}
