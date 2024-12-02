package com.github.wallev.farmsoulkitchen.client.gui.widget.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

// 防止覆盖tooltip的部分区域
public class TitleInfoButton extends Button {

    public TitleInfoButton(int pX, int pY, int pWidth, int pHeight, Component title) {
        super(pX, pY, pWidth, pHeight, title, (b) -> {}, Supplier::get);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return false;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        pGuiGraphics.drawString(mc.font, this.getMessage(), this.getX(), this.getY(), 0xFFFFFF, false);
    }
}
