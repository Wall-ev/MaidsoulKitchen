package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class NormalTooltipButton extends Button implements ITooltipButton {
    private List<Component> tooltips;

    public NormalTooltipButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, List<Component> tooltips, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
        this.tooltips = tooltips;
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered;
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {
        Screen screen = minecraft.screen;
        if (screen == null) {
            return;
        }

        screen.renderComponentTooltip(poseStack, this.tooltips, mouseX, mouseY);
    }

    public List<Component> getTooltips() {
        return tooltips;
    }

    public void setTooltips(List<Component> tooltips) {
        this.tooltips = tooltips;
    }
}
