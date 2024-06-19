package com.github.catbert.tlma.task.cook.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.Container;
import net.minecraftforge.items.ItemStackHandler;

public abstract class CookBeHandler {

    public EntityMaid maid;
    private ItemStackHandler beInventory;
    private Container container;

    public CookBeHandler() {
    }

    public void setMaid(EntityMaid maid) {
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public void setBeInventory(ItemStackHandler beInventory) {
        this.beInventory = beInventory;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public ItemStackHandler getBeInventory() {
        return beInventory;
    }

    public Container getContainer() {
        return container;
    }
}
