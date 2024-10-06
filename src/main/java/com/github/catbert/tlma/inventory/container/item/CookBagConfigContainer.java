package com.github.catbert.tlma.inventory.container.item;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CookBagConfigContainer extends CookBagAbstractContainer{
    public static final MenuType<CookBagConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookBagConfigContainer(windowId, inv, data.readItem()));
    public CookBagConfigContainer(int id, Inventory inventory, ItemStack cookBag) {
        super(TYPE, id, inventory, cookBag);
    }
}
