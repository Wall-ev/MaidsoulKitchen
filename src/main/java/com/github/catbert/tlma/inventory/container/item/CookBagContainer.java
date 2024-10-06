package com.github.catbert.tlma.inventory.container.item;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CookBagContainer extends CookBagAbstractContainer{
    public static final MenuType<CookBagContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookBagContainer(windowId, inv, data.readItem()));
    public CookBagContainer(int id, Inventory inventory, ItemStack cookBag) {
        super(TYPE, id, inventory, cookBag);
        addBagTypeSlots(container);
    }
}
