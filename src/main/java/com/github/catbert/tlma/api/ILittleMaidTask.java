package com.github.catbert.tlma.api;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.world.MenuProvider;

public interface ILittleMaidTask extends ILittleMaid, IMaidTask {

    @Override
    default void addMaidTask(TaskManager manager) {
        if (canLoaded()) {
            manager.add(this);
        }
    }

    boolean canLoaded();

    default MenuProvider getGuiProvider(EntityMaid maid, int entityId, boolean simulate) {
        return maid.getMaidBackpackType().getGuiProvider(maid.getId());
    }
}
