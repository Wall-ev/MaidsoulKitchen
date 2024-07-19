package com.github.catbert.tlma.inventory.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CookConfigerContainer extends TaskConfigerContainer {
    public static final MenuType<CookConfigerContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookConfigerContainer(windowId, inv, data.readInt()));

    public CookConfigerContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }
}
