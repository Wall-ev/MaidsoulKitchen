package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class TImageButton extends net.minecraft.client.gui.components.ImageButton implements ITooltipButton {
    private final ICookTask<?, ?> cookTask;
    public TImageButton(ICookTask<?, ?> cookTask, int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, int pYDiffTex, ResourceLocation pResourceLocation, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, pOnPress);
        this.cookTask = cookTask;
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered;
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {
        minecraft.screen.renderTooltip(poseStack, cookTask.getWarnComponent(), Optional.empty(), mouseX, mouseY);
    }
}
