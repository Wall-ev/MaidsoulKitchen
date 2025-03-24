package com.github.wallev.maidsoulkitchen.task.farm.handler.fruit;

import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;

public enum FruitHandlerManager implements IFarmHandlerManager<FruitHandler> {

    COMPAT(new CompatFruitHandler());

    private final FruitHandler fruitHandler;

    FruitHandlerManager(FruitHandler fruitHandler) {
        this.fruitHandler = fruitHandler;
    }

    public FruitHandler getFarmHandler() {
        return fruitHandler;
    }

}
