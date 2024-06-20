package com.github.catbert.tlma.client.gui.entity.maid;

import com.github.catbert.tlma.inventory.container.CookConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

//@IPNPlayerSideOnly
//@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
//@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
//@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
//@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
//@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CookConfigContainerGui extends AbstractMaidContainerGui<CookConfigContainer> {
    public CookConfigContainerGui(CookConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
}
