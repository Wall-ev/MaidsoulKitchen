package com.github.catbert.tlma.mixin.tomstorage;

import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController;
import com.jaquadro.minecraft.storagedrawers.util.ItemCollectionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BlockEntityController.class, remap = false)
public interface BlockEntityControllerAccessor {
    @Accessor
    ItemCollectionRegistry<SlotRecordAccessor> getDrawerPrimaryLookup();


}
