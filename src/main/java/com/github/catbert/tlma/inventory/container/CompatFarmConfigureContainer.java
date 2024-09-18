package com.github.catbert.tlma.inventory.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CompatFarmConfigureContainer extends TaskConfigureContainer {
    public static final MenuType<CompatFarmConfigureContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CompatFarmConfigureContainer(windowId, inv, data.readInt()));

    public CompatFarmConfigureContainer(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
