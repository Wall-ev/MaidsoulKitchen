package com.github.catbert.tlma.inventory.container.maid;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class FruitFarmConfigContainer2 extends TaskConfigContainer {
    public static final MenuType<FruitFarmConfigContainer2> TYPE = IForgeMenuType.create((windowId, inv, data) -> new FruitFarmConfigContainer2(windowId, inv, data.readInt()));

    public FruitFarmConfigContainer2(int id, Inventory inventory, int entity) {
        super(TYPE, id, inventory, entity);
    }
}
