package com.github.wallev.farmsoulkitchen.task.farm.handler.v1;

import com.github.wallev.farmsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.farmsoulkitchen.api.task.v1.farm.IHandlerInfo;

public interface IFarmHandlerManager<T extends ICompatFarmHandler & IHandlerInfo> {

    T getFarmHandler();

}
