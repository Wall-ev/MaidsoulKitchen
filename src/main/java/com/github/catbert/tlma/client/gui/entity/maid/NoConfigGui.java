package com.github.catbert.tlma.client.gui.entity.maid;

import com.github.catbert.tlma.inventory.container.maid.NoConfigContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class NoConfigGui extends MaidTaskConfigGui<NoConfigContainer> {
    public NoConfigGui(NoConfigContainer noConfigContainer, Inventory inv, Component titleIn) {
        super(noConfigContainer, inv, Component.empty());
    }

    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderAddition(graphics, mouseX, mouseY, partialTicks);
        renderNoConfigTip(graphics);
    }
}
