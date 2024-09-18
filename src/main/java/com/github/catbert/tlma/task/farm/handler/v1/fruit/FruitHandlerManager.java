package com.github.catbert.tlma.task.farm.handler.v1.fruit;

public enum FruitHandlerManager{

    FRUITSTACK(new FruitStackFruitHandler()),
    SIMPLE_FARMING(new SimpleFarmingFruitHandler()),
    VINERY(new VineryFruitHandler()),
    COMPAT(new CompatFruitHandler());

    private final FruitHandler fruitHandler;

    FruitHandlerManager(FruitHandler vineryBerryHandler) {
        this.fruitHandler = vineryBerryHandler;
    }

    public FruitHandler getFruitHandler() {
        return fruitHandler;
    }
}
