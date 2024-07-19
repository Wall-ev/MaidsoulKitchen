package com.github.catbert.tlma.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class CookConfigerContainer extends TaskConfigerContainer {
    public static final MenuType<CookConfigerContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> {
        FriendlyByteBuf data1 = data;
        int i = data1.readInt();
        String s = data1.readUtf();
        return new CookConfigerContainer(data.readUtf(), windowId, inv, data.readInt());
    });
    public final String taskUid;

    public CookConfigerContainer(String taskUid, int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
        this.taskUid = taskUid;
    }
}
