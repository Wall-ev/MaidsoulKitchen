package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;


import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite.FarmersRespiteBlackTeaBerryHandler;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite.FarmersRespiteGreenTeaBerryHandler;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite.FarmersRespiteYellowTeaBerryHandler;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

import static com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo.*;

public enum BerryHandlerManager implements IFarmHandlerManager<BerryHandler> {

    MINECRAFT(BERRY_MINECRAFT, VanillaBerryHandler::new),
    L2_HARVESTER(BERRY_L2_HARVESTER, L2BerryHandler::new),
    FARMERS_RESPITE_GREEN_TEA(BERRY_FARMERS_RESPITE_GREEN_TEA, FarmersRespiteGreenTeaBerryHandler::new),
    FARMERS_RESPITE_YELLOW_TEA(BERRY_FARMERS_RESPITE_YELLOW_TEA, FarmersRespiteYellowTeaBerryHandler::new),
    FARMERS_RESPITE_BLACK_TEA(BERRY_FARMERS_RESPITE_BLACK_TEA, FarmersRespiteBlackTeaBerryHandler::new),
    SIMPLE_FARMING(BERRY_SIMPLE_FARMING, SimpleFarmingBerryHandler::new),
    COMPAT(BERRY_COMPAT, CompatBerryHandler::new);

    public static final BerryHandlerManager[] VALUES = values();

    private final ResourceLocation uid;
    private final Mods bindMod;
    private final Supplier<BerryHandler> berryHandler;

    BerryHandlerManager(TaskInfo taskInfo, Supplier<BerryHandler> berryHandler) {
        this(taskInfo.getUid(), taskInfo.getBindMod(), berryHandler);
    }

    BerryHandlerManager(ResourceLocation uid, Mods bindMod, Supplier<BerryHandler> berryHandler) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.berryHandler = berryHandler;
    }

    BerryHandlerManager(String uid, Mods bindMod, Supplier<BerryHandler> berryHandler) {
        this(create(uid), bindMod, berryHandler);
    }

    static ResourceLocation create(String uid) {
        return VResourceLocation.of(MaidsoulKitchen.MOD_ID, uid);
    }

    public BerryHandler getFarmHandler() {
        return berryHandler.get();
    }

    public ResourceLocation getUid() {
        return uid;
    }

    public Mods getBindMod() {
        return bindMod;
    }
}
