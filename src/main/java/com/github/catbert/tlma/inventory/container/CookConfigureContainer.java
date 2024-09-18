package com.github.catbert.tlma.inventory.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CookConfigureContainer extends TaskConfigureContainer {
    public static final MenuType<CookConfigureContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookConfigureContainer(windowId, inv, data.readInt()));

    public CookConfigureContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }
}
