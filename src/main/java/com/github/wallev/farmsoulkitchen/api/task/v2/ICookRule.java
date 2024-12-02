package com.github.wallev.farmsoulkitchen.api.task.v2;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface ICookRule {

    static <B extends BlockEntity> boolean output(B be, IBeInv<B> beInv, IBaseCook<B, ?> baseCook) {
        return beInv.getStackInSlot(be, baseCook.getOutputSlot()).isEmpty();
    }

}
