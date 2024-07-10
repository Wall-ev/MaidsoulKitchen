package com.github.catbert.tlma.client.gui.widget.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public class NormalTooltipButton extends Button implements ITooltipBtn{
    private final List<Component> tooltips;

    public NormalTooltipButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, List<Component> tooltips, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, Supplier::get);
        this.tooltips = tooltips;
    }

    @Override
    public void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        graphics.renderComponentTooltip(mc.font, this.tooltips, mouseX, mouseY);
    }
}
