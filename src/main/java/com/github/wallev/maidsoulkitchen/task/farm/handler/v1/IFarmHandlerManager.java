package com.github.wallev.maidsoulkitchen.task.farm.handler.v1;

import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.IHandlerInfo;

public interface IFarmHandlerManager<T extends ICompatFarmHandler & IHandlerInfo> {

    T getFarmHandler();

}
