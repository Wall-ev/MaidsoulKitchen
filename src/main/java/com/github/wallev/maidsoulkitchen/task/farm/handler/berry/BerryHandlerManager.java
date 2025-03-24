package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;


import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite.FarmersRespiteBlackTeaBerryHandler;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite.FarmersRespiteGreenTeaBerryHandler;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite.FarmersRespiteYellowTeaBerryHandler;

public enum BerryHandlerManager implements IFarmHandlerManager<BerryHandler> {

    MINECRAFT(new VanillaBerryHandler()),
    FARMERS_RESPITE_GREEN_TEA(new FarmersRespiteGreenTeaBerryHandler()),
    FARMERS_RESPITE_YELLOW_TEA(new FarmersRespiteYellowTeaBerryHandler()),
    FARMERS_RESPITE_BLACK_TEA(new FarmersRespiteBlackTeaBerryHandler()),
    COMPAT(new CompatBerryHandler());

    private final BerryHandler berryHandler;

    BerryHandlerManager(BerryHandler berryHandler) {
        this.berryHandler = berryHandler;
    }

    public BerryHandler getFarmHandler() {
        return berryHandler;
    }
}
