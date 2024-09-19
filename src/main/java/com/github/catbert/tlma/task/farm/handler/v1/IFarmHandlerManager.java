package com.github.catbert.tlma.task.farm.handler.v1;

import com.github.catbert.tlma.api.task.v1.farm.ICompatFarmHandler;
import com.github.catbert.tlma.api.task.v1.farm.IHandlerInfo;

public interface IFarmHandlerManager<T extends ICompatFarmHandler & IHandlerInfo> {

    T getFarmHandler();

}
