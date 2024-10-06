package com.github.catbert.tlma.inventory.container.maid;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CompatFarmConfigContainer extends TaskConfigContainer {
    public static final MenuType<CompatFarmConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CompatFarmConfigContainer(windowId, inv, data.readInt()));

    public CompatFarmConfigContainer(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
