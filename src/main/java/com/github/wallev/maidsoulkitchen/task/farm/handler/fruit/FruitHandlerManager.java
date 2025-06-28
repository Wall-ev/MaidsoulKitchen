package com.github.wallev.maidsoulkitchen.task.farm.handler.fruit;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

import static com.github.wallev.maidsoulkitchen.task.TaskInfo.FRUIT_COMPAT;
import static com.github.wallev.maidsoulkitchen.task.TaskInfo.FRUIT_SIMPLE_FARMING;

public enum FruitHandlerManager implements IFarmHandlerManager<FruitHandler> {

    SIMPLE_FARMING(FRUIT_SIMPLE_FARMING, SimpleFarmingFruitHandler::new),
    COMPAT(FRUIT_COMPAT, CompatFruitHandler::new);

    public static final FruitHandlerManager[] VALUES = values();

    private final ResourceLocation uid;
    private final Mods bindMod;
    private final Supplier<FruitHandler> fruitHandler;

    FruitHandlerManager(TaskInfo taskInfo, Supplier<FruitHandler> berryHandler) {
        this(taskInfo.getUid(), taskInfo.getBindMod(), berryHandler);
    }

    FruitHandlerManager(ResourceLocation uid, Mods bindMod, Supplier<FruitHandler> berryHandler) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.fruitHandler = berryHandler;
    }

    FruitHandlerManager(String uid, Mods bindMod, Supplier<FruitHandler> berryHandler) {
        this(create(uid), bindMod, berryHandler);
    }

    static ResourceLocation create(String uid) {
        return VResourceLocation.create(MaidsoulKitchen.MOD_ID, uid);
    }

    public FruitHandler getFarmHandler() {
        return fruitHandler.get();
    }

    public ResourceLocation getUid() {
        return uid;
    }

    public Mods getBindMod() {
        return bindMod;
    }

}
