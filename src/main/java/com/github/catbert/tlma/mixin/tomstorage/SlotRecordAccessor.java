package com.github.catbert.tlma.mixin.tomstorage;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController$SlotRecord")
public interface SlotRecordAccessor{
    @Accessor
    BlockPos getCoord();
    @Accessor
    IDrawerGroup getGroup();
    @Accessor
    int getSlot();
    @Accessor
    int getIndex();
    @Accessor
    int getPriority();
}
