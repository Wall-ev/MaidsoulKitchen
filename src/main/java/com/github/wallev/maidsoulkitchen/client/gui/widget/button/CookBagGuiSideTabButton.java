package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.wallev.maidsoulkitchen.handler.VComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class CookBagGuiSideTabButton extends Button implements ITooltipButton {
    private static final ResourceLocation SIDE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/culinary_hub_gui_right_side.png");
    private static final int V_OFFSET = 107;
    private final List<Component> tooltips;
    private final int top;

    public CookBagGuiSideTabButton(int x, int y, int top, Button.OnPress onPressIn, List<Component> tooltips) {
        super(x, y, 26, 24, VComponent.empty(), onPressIn);
        this.top = V_OFFSET + top;
        this.tooltips = tooltips;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        if (!this.active) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SIDE);
            blit(poseStack, this.x + 2, this.y, 209, top, this.width, this.height, 256, 256);
        } else {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SIDE);
            blit(poseStack, this.x + 5, this.y, 235, top, this.width, this.height, 256, 256);
        }
        // 193, 111
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SIDE);
        blit(poseStack, this.x + 6, this.y + 4, 193, top + 4, 16, 16, 256, 256);

        if (this.isHoveredOrFocused()) {
            this.renderTooltip(poseStack, Minecraft.getInstance(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {
    }
    
    @Override
    public boolean isTooltipHovered() {
        return this.isHovered;
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft mc, int mouseX, int mouseY) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            return;
        }

        screen.renderComponentTooltip(poseStack, this.tooltips, mouseX, mouseY);
    }
}