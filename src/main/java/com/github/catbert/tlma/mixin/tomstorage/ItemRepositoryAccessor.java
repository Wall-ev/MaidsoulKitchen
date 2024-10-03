package com.github.catbert.tlma.mixin.tomstorage;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemRepository;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController$ItemRepository")
public abstract class ItemRepositoryAccessor extends DrawerItemRepository {
    public ItemRepositoryAccessor(IDrawerGroup group) {
        super(group);
    }


}
