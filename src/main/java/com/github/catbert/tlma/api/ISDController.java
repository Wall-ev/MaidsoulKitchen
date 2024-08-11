package com.github.catbert.tlma.api;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.util.ItemCollectionRegistry;
import net.minecraftforge.common.util.LazyOptional;

public interface ISDController {

    ItemCollectionRegistry<?> getDrawerPrimaryLookup();

    LazyOptional<IDrawerGroup> getCapabilityGroup();

}
