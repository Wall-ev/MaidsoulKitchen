package com.github.wallev.farmsoulkitchen.client.gui.entity.maid;

import com.github.wallev.farmsoulkitchen.client.gui.widget.button.TitleInfoButton;
import com.github.wallev.farmsoulkitchen.client.gui.widget.button.Zone;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public abstract class MaidTaskConfigGui<T extends TaskConfigContainer> extends com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.MaidTaskConfigGui<T> {
    protected final int titleStartY = 8;
    protected Zone visualZone = new Zone(leftPos + 81, topPos + 28, 176, 137);;
    protected int solIndex = 0;

    public MaidTaskConfigGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    protected void initAdditionWidgets() {
        visualZone = new Zone(leftPos + 81, topPos + 28, 176, 137);

        this.addTitleInfoButton();
    }

    protected void addTitleInfoButton() {
        int titleStartX = visualZone.startX() + (visualZone.width() - font.width(this.title)) / 2;
        TitleInfoButton titleInfoButton = new TitleInfoButton(titleStartX, visualZone.startY() + titleStartY, font.width(this.title), 9, this.title);
        this.addRenderableWidget(titleInfoButton);
    }

    protected void renderNoConfigTip(GuiGraphics graphics) {
        int color = Color.YELLOW.getRGB();
        MutableComponent translatable = Component.translatable("gui.farmsoulkitchen.config.no_config").withStyle(ChatFormatting.ITALIC);
        int startX = ((visualZone.width() - font.width(translatable)) / 2) + visualZone.startX();
        int startY = ((visualZone.height() - font.lineHeight ) / 2) + visualZone.startY();
        graphics.drawString(font, translatable, startX, startY, color, false);
        graphics.fill(startX, startY + font.lineHeight + 1, startX + font.width(translatable), startY + font.lineHeight + 2, color);
    }
}
