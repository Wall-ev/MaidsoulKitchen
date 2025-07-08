package com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.ITaskDataKey;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v0.BerryData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("deprecation")
public class TaskBerryDataKey extends ITaskDataKey<BerryFruitData> {

    public TaskBerryDataKey() {
        super(new BerryData.Serializer());
    }

    @Override
    public ResourceLocation getKey() {
        return TaskInfo.BERRY_FARM.getUid();
    }

    @Override
    public Codec<BerryFruitData> codec() {
        return BerryFruitData.CODEC;
    }

    @Override
    public BerryFruitData defaultData() {
        return BerryFruitData.createDefaultBerry();
    }
}
