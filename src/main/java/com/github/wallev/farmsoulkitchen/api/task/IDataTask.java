package com.github.wallev.farmsoulkitchen.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public interface IDataTask<D> {
    TaskDataKey<D> getCookDataKey();

    D getDefaultData();

    default D getTaskData(EntityMaid maid) {
       return maid.getOrCreateData(getCookDataKey(), getDefaultData());
    }
}
