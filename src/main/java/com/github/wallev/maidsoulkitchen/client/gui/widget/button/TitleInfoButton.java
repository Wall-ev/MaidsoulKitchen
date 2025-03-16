package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

// 防止覆盖tooltip的部分区域
public class TitleInfoButton extends Button {

    public TitleInfoButton(int pX, int pY, int pWidth, int pHeight, Component title) {
        super(pX, pY, pWidth, pHeight, title, (b) -> {});
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return false;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        mc.font.draw(poseStack, this.getMessage(), x, y, 0xFFFFFF);
    }
}
