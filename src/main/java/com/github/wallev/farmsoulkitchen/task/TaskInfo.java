package com.github.wallev.farmsoulkitchen.task;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import net.minecraft.resources.ResourceLocation;

public enum TaskInfo {
    BERRY_FARM("berries_farm");
    public final ResourceLocation uid;

    TaskInfo(ResourceLocation uid) {
        this.uid = uid;
    }
    TaskInfo(String uid) {
        this.uid = new ResourceLocation(FarmsoulKitchen.MOD_ID, uid);
    }
}
