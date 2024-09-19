package com.github.catbert.tlma.task.farm.handler.v1.berry;


import com.github.catbert.tlma.task.farm.handler.v1.IFarmHandlerManager;

public enum BerryHandlerManager implements IFarmHandlerManager<BerryHandler> {

    MINECRAFT(new VanillaBerryHandler()),
    SIMPLE_FARMING(new SimpleFarmingBerryHandler()),
    VINERY(new VineryBerryHandler()),
    COMPAT(new CompatBerryHandler());

    private final BerryHandler vineryBerryHandler;

    BerryHandlerManager(BerryHandler vineryBerryHandler) {
        this.vineryBerryHandler = vineryBerryHandler;
    }

    public BerryHandler getFarmHandler() {
        return vineryBerryHandler;
    }
}
