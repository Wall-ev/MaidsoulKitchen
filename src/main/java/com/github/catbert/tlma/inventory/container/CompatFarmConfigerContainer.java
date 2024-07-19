package com.github.catbert.tlma.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CompatFarmConfigerContainer extends TaskConfigerContainer {
    public static final MenuType<CompatFarmConfigerContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> {
//        FriendlyByteBuf data1 = data;
//        int i = data1.readInt();
//        int s = data1.readInt();
//
//        String s1 = data.readUtf();
//        String[] split = s1.split(",");

        return new CompatFarmConfigerContainer(windowId, inv, data.readInt());
    });
    public final int taskUid;

    public CompatFarmConfigerContainer(int id, Inventory inventory, int string) {
        super(TYPE, id, inventory, string - 2);
        this.taskUid = string - 2 + 2;
    }
}
