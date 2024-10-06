package com.github.catbert.tlma.inventory.container.maid;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CompatMelonConfigContainer extends TaskConfigContainer {
    public static final MenuType<CompatMelonConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CompatMelonConfigContainer(windowId, inv, data.readInt()));

    public CompatMelonConfigContainer(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
