package com.github.catbert.tlma.client.gui.widget.button;

import com.github.catbert.tlma.TLMAddon;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MaidSideTabButton extends Button {
    private static final ResourceLocation RIGHT_SIDE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/maid_gui_right_side.png");
    private final Component title;
    private final int top;

    public MaidSideTabButton(int x, int y, int top, OnPress onPressIn, Component title) {
        super(Button.builder(Component.empty(), onPressIn).pos(x, y).size(26, 24));
        this.top = top;
        this.title = title;
    }


    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        if (!this.active) {
            graphics.blit(RIGHT_SIDE, this.getX(), this.getY(), 209, top, this.width, this.height, 256, 256);
        }
        // 193, 111
        graphics.blit(RIGHT_SIDE, this.getX() + 6, this.getY() + 4, 193, top + 4, 16, 16, 256, 256);
        renderToolTip(graphics, minecraft, mouseX, mouseY);
    }

    public void renderToolTip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        if (this.isHovered()) {
            graphics.renderTooltip(mc.font, this.title, mouseX, mouseY);
        }
    }

}