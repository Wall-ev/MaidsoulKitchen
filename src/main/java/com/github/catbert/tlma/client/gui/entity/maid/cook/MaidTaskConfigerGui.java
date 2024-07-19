package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.inventory.container.TaskConfigerContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MaidTaskConfigerGui<T extends TaskConfigerContainer> extends AbstractMaidContainerGui<T> implements IAddonAbstractMaidContainerGui{
    public MaidTaskConfigerGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
}
