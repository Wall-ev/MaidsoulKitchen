package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.handler.VComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class TypeButton extends NormalTooltipButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    protected boolean isSelected;
    protected String modeUid;

    public TypeButton(int pX, int pY, int pWidth, int pHeight, boolean isSelected) {
        super(pX, pY, pWidth, pHeight, Component.empty(), Collections.emptyList(), (b) -> {});
        this.isSelected = isSelected;
        this.setModeUid(isSelected);
    }

    protected void toggleState() {
        this.isSelected = !this.isSelected;
        this.setModeUid(this.isSelected);
    }

    private void setModeUid(boolean isSelected) {
        this.modeUid = isSelected ? CookData.Mode.WHITELIST.name : CookData.Mode.BLACKLIST.name;
    }

    @Override
    protected void renderBg(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, x, y, 0, 232, 18, 18, 256, 256);
        if (isSelected) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, x + 1, y + 1, 16, 197, 16, 16, 256, 256);
        }else {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, x + 1, y + 1, 16, 181, 16, 16, 256, 256);
        }
    }

    @Override
    public Component getMessage() {
        return Component.empty();
//        return VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.modeUid));
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {
        Screen screen = minecraft.screen;
        if (screen == null) {
            return;
        }
        List<Component> translatable = List.of(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s.desc.0", this.modeUid)),
                VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s.desc.1", this.modeUid)).withStyle(ChatFormatting.GRAY));
        screen.renderComponentTooltip(poseStack, translatable, mouseX, mouseY);
    }
}
