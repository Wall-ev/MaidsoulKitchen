package com.github.wallev.maidsoulkitchen.task.farm.handler.v2.fruit;

import com.github.wallev.maidsoulkitchen.handler.VComponent;
import net.minecraft.network.chat.MutableComponent;

public enum CompatManager {

    SIMPLE_FARMING(VComponent.translatable("ss"), new SimpleFarmingFruitHandler()),
    ;

    private final MutableComponent ss;
    private final ICompatFarmHandler simpleFarmingFruitHandler;

    CompatManager(MutableComponent ss, ICompatFarmHandler simpleFarmingFruitHandler) {
        this.ss = ss;
        this.simpleFarmingFruitHandler = simpleFarmingFruitHandler;
    }

    public MutableComponent getSS() {
        return ss;
    }

    public ICompatFarmHandler getSimpleFarmingFruitHandler() {
        return simpleFarmingFruitHandler;
    }
}
