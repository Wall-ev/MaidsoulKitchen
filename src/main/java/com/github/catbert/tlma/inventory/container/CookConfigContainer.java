package com.github.catbert.tlma.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CookConfigContainer extends TaskConfigContainer {
    public static final MenuType<CookConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookConfigContainer(windowId, inv, data.readInt()));

    public CookConfigContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }
}
