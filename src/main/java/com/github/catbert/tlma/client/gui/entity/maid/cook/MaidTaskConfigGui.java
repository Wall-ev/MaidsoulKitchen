package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.client.gui.widget.button.TitleInfoButton;
import com.github.catbert.tlma.client.gui.widget.button.Zone;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MaidTaskConfigGui<T extends TaskConfigContainer> extends com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.MaidTaskConfigGui<T> {
    protected final int titleStartY = 8;
    protected Zone visualZone;
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
}
