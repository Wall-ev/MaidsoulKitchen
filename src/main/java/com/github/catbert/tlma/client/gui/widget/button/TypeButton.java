package com.github.catbert.tlma.client.gui.widget.button;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class TypeButton extends NormalTooltipButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/cook_guide.png");
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
        this.modeUid = isSelected ? CookData.Mode.SELECT.name : CookData.Mode.RANDOM.name;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.blit(TEXTURE, getX(), getY(), 0, 232, 18, 18);
        if (isSelected) {
            pGuiGraphics.blit(TEXTURE, getX() + 1, getY() + 1, 16, 197, 16, 16);
        }else {
            pGuiGraphics.blit(TEXTURE, getX() + 1, getY() + 1, 16, 181, 16, 16);
        }
    }

    @Override
    public Component getMessage() {
        return Component.translatable(String.format("gui.touhou_little_maid_addon.btn.cook_guide.type.%s", this.modeUid));
    }

    @Override
    public void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        graphics.renderComponentTooltip(mc.font, List.of(Component.translatable(String.format("gui.touhou_little_maid_addon.btn.cook_guide.type.%s.desc", this.modeUid))), mouseX, mouseY);
    }
}
