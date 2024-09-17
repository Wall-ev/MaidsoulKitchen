package com.github.catbert.tlma.client.gui.widget.button;

import com.github.catbert.tlma.entity.passive.CookTaskData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public class TypeButton2 extends NormalTooltipButton {
    protected boolean isSelected;
    protected String modeUid;

    public TypeButton2(int pX, int pY, int pWidth, int pHeight, boolean isSelected) {
        super(pX, pY, pWidth, pHeight, Component.empty(), Collections.emptyList(), (b) -> {});
        this.isSelected = isSelected;
        this.setModeUid(isSelected);
    }

    protected void toggleSelected() {
        this.isSelected = !this.isSelected;
        this.setModeUid(this.isSelected);
    }

    private void setModeUid(boolean isSelected) {
        this.modeUid = isSelected ? CookTaskData.Mode.SELECT.getUid() : CookTaskData.Mode.RANDOM.getUid();
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
