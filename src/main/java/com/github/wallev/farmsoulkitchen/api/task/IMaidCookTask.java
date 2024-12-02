package com.github.wallev.farmsoulkitchen.api.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IMaidCookTask {

    void tryToExtract(BlockEntity blockEntity, EntityMaid entityMaid);

    void tryToCook(BlockEntity blockEntity, EntityMaid entityMaid);

    default void tryToFuel(BlockEntity blockEntity, EntityMaid entityMaid) {

    }
}
