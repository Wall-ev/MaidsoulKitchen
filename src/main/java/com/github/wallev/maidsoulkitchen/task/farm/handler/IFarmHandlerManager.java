package com.github.wallev.maidsoulkitchen.task.farm.handler;

import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandlerInfo;

public interface IFarmHandlerManager<T extends ICompatFarmHandler & ICompatHandlerInfo> {

    T getFarmHandler();

}
