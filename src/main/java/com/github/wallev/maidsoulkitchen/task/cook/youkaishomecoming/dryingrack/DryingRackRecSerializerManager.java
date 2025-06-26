package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.dryingrack;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;

@TaskClassAnalyzer(TaskInfo.YHC_DRYING_RACK)
public class DryingRackRecSerializerManager extends RecSerializerManager<DryingRackRecipe> {
    private static final DryingRackRecSerializerManager INSTANCE = new DryingRackRecSerializerManager();

    protected DryingRackRecSerializerManager() {
        super(YHBlocks.RACK_RT.get());
    }

    public static DryingRackRecSerializerManager getInstance() {
        return INSTANCE;
    }
}
