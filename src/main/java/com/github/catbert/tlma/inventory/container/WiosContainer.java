package com.github.catbert.tlma.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class WiosContainer extends WirelessIOContainer {
    public static final MenuType<WiosContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new WiosContainer(windowId, inv, data.readItem()));
    public WiosContainer(int id, Inventory inventory, ItemStack wirelessIO) {
        super(id, inventory, wirelessIO);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().getItem() instanceof ItemWirelessIO;
    }
}