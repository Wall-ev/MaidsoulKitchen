package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.client.gui.widget.button.Zone;
import com.github.catbert.tlma.inventory.container.TaskConfigerContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MaidTaskConfigerGui<T extends TaskConfigerContainer> extends AbstractMaidContainerGui<T> implements IAddonAbstractMaidContainerGui{
    protected Zone visualZone;

    public MaidTaskConfigerGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        if (getMaid() != null) {
            this.guiCoordInfoInit();
        }
    }

    protected void guiCoordInfoInit() {
        visualZone = new Zone(leftPos + 81, topPos + 28, 176, 137);
    }
}
