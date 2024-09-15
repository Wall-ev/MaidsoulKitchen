package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.client.gui.entity.maid.IAbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = AbstractMaidContainer.class, remap = false)
public abstract class MixinAbstractMaidContainer implements IAbstractMaidContainer {

    private int TASK_PAGE$tlma = 0;
    private boolean taskListOpen$tlma = false;

    @Override
    public void setTaskListOpen(boolean taskListOpen$tlma) {
        this.taskListOpen$tlma = taskListOpen$tlma;
    }

    @Override
    public boolean getTaskListOpen() {
        return this.taskListOpen$tlma;
    }

    @Override
    public void setTaskPage(int taskPage) {
        this.TASK_PAGE$tlma = taskPage;
    }

    @Override
    public int getTaskPage() {
        return this.TASK_PAGE$tlma;
    }
}
