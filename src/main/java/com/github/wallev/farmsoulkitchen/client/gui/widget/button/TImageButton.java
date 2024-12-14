package com.github.wallev.farmsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

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
    public void renderTooltip(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        guiGraphics.renderComponentTooltip(minecraft.font, cookTask.getWarnComponent(), mouseX, mouseY);
    }
}
