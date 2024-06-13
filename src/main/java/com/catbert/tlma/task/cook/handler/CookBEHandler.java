package com.catbert.tlma.task.cook.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class CookBEHandler<B extends BlockEntity> {

    public final ItemStackHandler beInventory;

    public final EntityMaid maid;

    public CookBEHandler(ItemStackHandler beInventory, EntityMaid maid) {
        this.beInventory = beInventory;
        this.maid = maid;
    }
}
