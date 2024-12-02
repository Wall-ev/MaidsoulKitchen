package com.github.wallev.farmsoulkitchen.inventory.container.maid;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class BerryFarmConfigContainer extends TaskConfigContainer {
    public static final MenuType<BerryFarmConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new BerryFarmConfigContainer(windowId, inv, data.readInt()));

    public BerryFarmConfigContainer(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
