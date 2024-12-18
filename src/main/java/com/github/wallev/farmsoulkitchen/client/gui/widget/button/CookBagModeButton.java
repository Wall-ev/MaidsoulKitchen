package com.github.wallev.farmsoulkitchen.client.gui.widget.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class CookBagModeButton extends Button {
    public CookBagModeButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, Supplier::get);
    }

    public CookBagModeButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, Tooltip tooltip) {
        this(pX, pY, pWidth, pHeight, pMessage, pOnPress);
        this.setTooltip(tooltip);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
