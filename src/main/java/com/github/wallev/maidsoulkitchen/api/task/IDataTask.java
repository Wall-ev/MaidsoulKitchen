package com.github.wallev.maidsoulkitchen.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;

public interface IDataTask<D> {
    TaskDataKey<D> getCookDataKey();

    @SuppressWarnings("unchecked")
    default D getDefaultData() {
        TaskDataKey<D> cookDataKey = getCookDataKey();
        ResourceLocation key = cookDataKey.getKey();
        IDataTask<D> value = (IDataTask<D>) TaskDataRegister.getValue(key);
        return value.getDefaultData();
    }

    default D getTaskData(EntityMaid maid) {
       return maid.getOrCreateData(getCookDataKey(), getDefaultData());
    }
}
