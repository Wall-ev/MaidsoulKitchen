package com.github.catbert.tlma.client.gui.widget.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public interface ITooltipBtn {
    boolean isHovered();
    void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY);
}
