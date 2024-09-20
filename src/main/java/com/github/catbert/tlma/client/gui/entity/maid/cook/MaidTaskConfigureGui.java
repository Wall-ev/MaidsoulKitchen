package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.client.gui.entity.maid.IAbstractMaidContainerGui;
import com.github.catbert.tlma.client.gui.widget.button.TitleInfoButton;
import com.github.catbert.tlma.client.gui.widget.button.Zone;
import com.github.catbert.tlma.entity.passive.SideTab;
import com.github.catbert.tlma.inventory.container.TaskConfigureContainer;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.MaidTaskMessage;
import com.github.catbert.tlma.network.message.RefreshMaidBrainMessage;
import com.github.catbert.tlma.network.message.ToggleSideTabMessage;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MaidTaskConfigureGui<T extends TaskConfigureContainer> extends AbstractMaidContainerGui<T>{
    protected Zone visualZone;
    protected final int titleStartY = 8;
    protected int solIndex = 0;
    protected final EntityMaid maid;
    protected final IMaidTask task;

    public MaidTaskConfigureGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.maid = getMenu().getMaid();
        this.task = this.maid.getTask();
    }

    @Override
    protected void init() {
        super.init();
        if (this.maid != null) {
            this.initAdditionData();
            this.initAdditionGuiInfo();
        }
    }

    protected void initAdditionData() {
    }

    protected void initAdditionGuiInfo() {
        visualZone = new Zone(leftPos + 81, topPos + 28, 176, 137);

        this.addTitleInfoButton();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.maid != null) {
            this.renderAddition(graphics, mouseX, mouseY, partialTicks);
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    protected void taskButtonPressed(IMaidTask maidTask, boolean enable) {
        NetworkHandler.sendToServer(new MaidTaskMessage(this.maid.getId(), maidTask.getUid()));
        if (maidTask instanceof ILittleMaidTask && maidTask.isEnable(this.maid)) {
            NetworkHandler.sendToServer(new ToggleSideTabMessage(this.getMenu().containerId, this.maid.getId(), SideTab.TASK_SETTING.getIndex(), maidTask.getUid(), this.isTaskListOpen(), ((IAbstractMaidContainerGui)this).getTaskPage()));
        }
    }

    protected void addTitleInfoButton() {
        int titleStartX = visualZone.startX() + (visualZone.width() - font.width(this.title)) / 2;
        TitleInfoButton titleInfoButton = new TitleInfoButton(titleStartX, visualZone.startY() + titleStartY, font.width(this.title), 9, this.title);
        this.addRenderableWidget(titleInfoButton);
    }

    @Override
    public void onClose() {
        super.onClose();
        NetworkHandler.sendToServer(new RefreshMaidBrainMessage(maid.getId()));
    }
}
