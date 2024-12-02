package com.github.wallev.farmsoulkitchen.inventory.container.maid;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class NoConfigContainer extends TaskConfigContainer {
    public static final MenuType<NoConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new NoConfigContainer(windowId, inv, data.readInt()));

    public NoConfigContainer(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
