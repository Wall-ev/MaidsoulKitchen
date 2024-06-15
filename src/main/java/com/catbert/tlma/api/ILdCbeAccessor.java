package com.catbert.tlma.api;

import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface ILdCbeAccessor<B extends ImplementedInventory, R extends Recipe<? extends Container>> {

    boolean canCraft$tlma(R rec, RegistryAccess access);

}
