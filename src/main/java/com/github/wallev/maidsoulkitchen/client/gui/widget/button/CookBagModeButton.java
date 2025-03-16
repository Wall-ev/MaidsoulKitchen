package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;


public class CookBagModeButton extends Button {
    public CookBagModeButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    public CookBagModeButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, List<Component> tooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, (Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) -> {
            Screen screen = Minecraft.getInstance().screen;
            if (screen == null) {
                return;
            }

            screen.renderComponentTooltip(pPoseStack, tooltip, pMouseX, pMouseY);
        });
    }

    public CookBagModeButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, Component tooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, (Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) -> {
            Screen screen = Minecraft.getInstance().screen;
            if (screen == null) {
                return;
            }

            screen.renderComponentTooltip(pPoseStack, Lists.newArrayList(tooltip), pMouseX, pMouseY);
        });
    }
}
