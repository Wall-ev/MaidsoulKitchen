package com.github.wallev.farmsoulkitchen.task.farm.handler.v1.fruit;

import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.IFarmHandlerManager;

public enum FruitHandlerManager implements IFarmHandlerManager<FruitHandler> {

    SIMPLE_FARMING(new SimpleFarmingFruitHandler()),
    COMPAT(new CompatFruitHandler());

    private final FruitHandler fruitHandler;

    FruitHandlerManager(FruitHandler fruitHandler) {
        this.fruitHandler = fruitHandler;
    }

    public FruitHandler getFarmHandler() {
        return fruitHandler;
    }

}
