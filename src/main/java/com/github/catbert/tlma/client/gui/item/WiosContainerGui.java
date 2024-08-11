package com.github.catbert.tlma.client.gui.item;

import com.github.catbert.tlma.inventory.container.WiosContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.WirelessIOContainerGui;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNIgnore;

@IPNIgnore
public class WiosContainerGui extends WirelessIOContainerGui {
    public WiosContainerGui(WiosContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
    }
}
