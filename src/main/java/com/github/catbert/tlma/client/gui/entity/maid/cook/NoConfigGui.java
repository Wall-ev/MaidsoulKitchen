package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.inventory.container.NoConfigContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import java.awt.*;

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

    private void renderNoConfigTip(GuiGraphics graphics) {
        int color = Color.YELLOW.getRGB();
        MutableComponent translatable = Component.translatable("gui.touhou_little_maid_addon.config.no_config").withStyle(ChatFormatting.ITALIC);
        int startX = ((visualZone.width() - font.width(translatable)) / 2) + visualZone.startX();
        int startY = ((visualZone.height() - font.lineHeight ) / 2) + visualZone.startY();
        graphics.drawString(font, translatable, startX, startY, color, false);
        graphics.fill(startX, startY + font.lineHeight + 1, startX + font.width(translatable), startY + font.lineHeight + 2, color);
    }
}
