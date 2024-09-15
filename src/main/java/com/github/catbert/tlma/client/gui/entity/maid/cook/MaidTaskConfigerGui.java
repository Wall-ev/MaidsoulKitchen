package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.client.gui.entity.maid.IAbstractMaidContainerGui;
import com.github.catbert.tlma.client.gui.widget.button.Zone;
import com.github.catbert.tlma.entity.passive.SideTabIndex;
import com.github.catbert.tlma.inventory.container.TaskConfigerContainer;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.ToggleSideTabMessage;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MaidTaskConfigerGui<T extends TaskConfigerContainer> extends AbstractMaidContainerGui<T> implements IAddonAbstractMaidContainerGui{
    protected Zone visualZone;
    protected final EntityMaid maid;

    public MaidTaskConfigerGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.maid = getMenu().getMaid();
    }

    @Override
    protected void init() {
        super.init();
        if (getMaid() != null) {
            this.guiCoordInfoInit();
        }
    }

    @Override
    protected void taskButtonPressed(IMaidTask maidTask, boolean enable) {
        super.taskButtonPressed(maidTask, enable);
        if (maidTask instanceof ILittleMaidTask && maidTask.isEnable(this.maid)) {
            NetworkHandler.CHANNEL.sendToServer(new ToggleSideTabMessage(this.getMenu().containerId, this.maid.getId(), SideTabIndex.SETTING.getIndex(), maidTask.getUid(), this.isTaskListOpen(), ((IAbstractMaidContainerGui)this).getTaskPage()));
        }
    }

    protected void guiCoordInfoInit() {
        visualZone = new Zone(leftPos + 81, topPos + 28, 176, 137);
    }
}
