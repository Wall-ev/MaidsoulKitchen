package com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.ITaskDataKey;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public class TaskCookDataKey extends ITaskDataKey<KitchenData> {
    @Override
    public Codec<KitchenData> codec() {
        return KitchenData.CODEC;
    }

    @Override
    public KitchenData defaultData() {
        return new KitchenData();
    }

    @Override
    public ResourceLocation getKey() {
        return TaskInfo.COOK.getUid();
    }
}
