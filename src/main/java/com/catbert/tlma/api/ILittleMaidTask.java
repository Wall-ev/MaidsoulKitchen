package com.catbert.tlma.api;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

public interface ILittleMaidTask extends ILittleMaid, IMaidTask {

    @Override
    default void addMaidTask(TaskManager manager) {
        if (canLoaded()) {
            manager.add(this);
        }
    }

    boolean canLoaded();

    /**
     * 默认好感度二级才可以启用任务
     * 当然得等酒石酸把这个用上去才会生效...
     */
    @Override
    default boolean isEnable(EntityMaid maid) {
        return maid.getFavorability() >= 2;
    }
}
