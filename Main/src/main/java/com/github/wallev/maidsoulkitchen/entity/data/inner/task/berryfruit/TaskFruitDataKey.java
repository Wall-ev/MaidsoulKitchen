package com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.ITaskDataKey;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v0.FruitData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("deprecation")
public class TaskFruitDataKey extends ITaskDataKey<BerryFruitData> {
    public TaskFruitDataKey() {
        super(new FruitData.Serializer());
    }

    @Override
    public ResourceLocation getKey() {
        return TaskInfo.FRUIT_FARM.getUid();
    }

    @Override
    public Codec<BerryFruitData> codec() {
        return BerryFruitData.CODEC;
    }

    @Override
    public BerryFruitData defaultData() {
        return BerryFruitData.createDefaultFruit();
    }
}
