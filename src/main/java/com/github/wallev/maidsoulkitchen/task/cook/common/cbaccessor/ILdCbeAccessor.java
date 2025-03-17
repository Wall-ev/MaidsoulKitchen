package com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor;

import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface ILdCbeAccessor<B extends ImplementedInventory, R extends Recipe<? extends Container>> {

    boolean tlmk$canCraft(R rec, RegistryAccess access);

}
