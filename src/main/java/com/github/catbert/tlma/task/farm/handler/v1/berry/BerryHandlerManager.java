package com.github.catbert.tlma.task.farm.handler.v1.berry;


public enum BerryHandlerManager{

    MINECRAFT(new VanillaBerryHandler()),
    SIMPLE_FARMING(new SimpleFarmingBerryHandler()),
    VINERY(new VineryBerryHandler()),
    COMPAT(new CompatBerryHandler());

    private final BerryHandler vineryBerryHandler;

    BerryHandlerManager(BerryHandler vineryBerryHandler) {
        this.vineryBerryHandler = vineryBerryHandler;
    }

    public BerryHandler getVineryBerryHandler() {
        return vineryBerryHandler;
    }
}
