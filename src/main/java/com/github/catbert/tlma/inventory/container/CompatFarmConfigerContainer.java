package com.github.catbert.tlma.inventory.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CompatFarmConfigerContainer extends TaskConfigerContainer {
    public static final MenuType<CompatFarmConfigerContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CompatFarmConfigerContainer(windowId, inv, data.readInt()));

    public CompatFarmConfigerContainer(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
