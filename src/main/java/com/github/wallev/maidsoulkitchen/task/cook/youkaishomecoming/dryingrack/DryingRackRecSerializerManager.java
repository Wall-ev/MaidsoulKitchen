package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.dryingrack;

import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;

public class DryingRackRecSerializerManager extends RecSerializerManager<DryingRackRecipe> {
    private static final DryingRackRecSerializerManager INSTANCE = new DryingRackRecSerializerManager();

    protected DryingRackRecSerializerManager() {
        super(YHBlocks.RACK_RT.get());
    }

    public static DryingRackRecSerializerManager getInstance() {
        return INSTANCE;
    }
}
