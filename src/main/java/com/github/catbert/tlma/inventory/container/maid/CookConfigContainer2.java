package com.github.catbert.tlma.inventory.container.maid;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CookConfigContainer2 extends TaskConfigContainer {
    public static final MenuType<CookConfigContainer2> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookConfigContainer2(windowId, inv, data.readInt()));

    public CookConfigContainer2(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }
}
