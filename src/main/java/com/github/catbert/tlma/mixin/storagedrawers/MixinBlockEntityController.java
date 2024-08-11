package com.github.catbert.tlma.mixin.storagedrawers;

import com.github.catbert.tlma.api.ISDController;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController;
import com.jaquadro.minecraft.storagedrawers.util.ItemCollectionRegistry;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BlockEntityController.class, remap = false)
public class MixinBlockEntityController implements ISDController {

    @Shadow @Final
    private ItemCollectionRegistry<?> drawerPrimaryLookup;

    @Shadow @Final
    private LazyOptional<IDrawerGroup> capabilityGroup;

//    @Shadow protected List<BlockEntityController.SlotRecord> drawerSlotList;

    public ItemCollectionRegistry<?> getDrawerPrimaryLookup() {
        return drawerPrimaryLookup;
    }

    public LazyOptional<IDrawerGroup> getCapabilityGroup() {
        return capabilityGroup;
    }
}
