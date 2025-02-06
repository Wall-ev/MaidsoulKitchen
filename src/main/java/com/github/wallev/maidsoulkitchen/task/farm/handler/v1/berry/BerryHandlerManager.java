package com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry;


import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.IFarmHandlerManager;
import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry.farmersrespite.FarmersRespiteBlackTeaBerryHandler;
import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry.farmersrespite.FarmersRespiteGreenTeaBerryHandler;
import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry.farmersrespite.FarmersRespiteYellowTeaBerryHandler;

public enum BerryHandlerManager implements IFarmHandlerManager<BerryHandler> {

    MINECRAFT(new VanillaBerryHandler()),
    FARMERS_RESPITE_GREEN_TEA(new FarmersRespiteGreenTeaBerryHandler()),
    FARMERS_RESPITE_YELLOW_TEA(new FarmersRespiteYellowTeaBerryHandler()),
    FARMERS_RESPITE_BLACK_TEA(new FarmersRespiteBlackTeaBerryHandler()),
    SIMPLE_FARMING(new SimpleFarmingBerryHandler()),
    COMPAT(new CompatBerryHandler());

    private final BerryHandler berryHandler;

    BerryHandlerManager(BerryHandler berryHandler) {
        this.berryHandler = berryHandler;
    }

    public BerryHandler getFarmHandler() {
        return berryHandler;
    }
}
