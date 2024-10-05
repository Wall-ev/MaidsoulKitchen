package com.github.catbert.tlma.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class FruitFarmConfigContainer extends TaskConfigContainer {
    public static final MenuType<FruitFarmConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new FruitFarmConfigContainer(windowId, inv, data.readInt()));

    public FruitFarmConfigContainer(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
