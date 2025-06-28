package com.github.wallev.maidsoulkitchen.task.farm.handler;

import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandlerInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IFarmHandlerManager<T extends ICompatFarmHandler & ICompatHandlerInfo> {

    Map<ResourceLocation, List<IFarmHandlerManager<?>>> HANDLER_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    static <FM extends ICompatFarmHandler & ICompatHandlerInfo, T extends IFarmHandlerManager<FM>> List<T> getHandlerManagers(ResourceLocation taskUid) {
        return (List<T>) HANDLER_MAP.get(taskUid);
    }

    static void registerHandler(ResourceLocation taskUid, List<IFarmHandlerManager<?>> handlerManagers) {
        HANDLER_MAP.put(taskUid, handlerManagers);
    }

    T getFarmHandler();

}
