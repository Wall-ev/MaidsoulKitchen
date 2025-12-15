package com.github.wallev.maidsoulkitchenlegacy.task.cook.youkaishomecoming.ferment;

import com.github.wallev.maidsoulkitchen.legacy.task.AutoLegacyCookTaskRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.ferment.TaskYhcFermentationTank;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationRecipe;

@AutoLegacyCookTaskRegister(TaskInfo.YHC_FERMENTATION_TANK_LEGACY)
public class TaskYhcFermentationTankLegacy extends TaskYhcFermentationTank {

    @Override
    protected RecSerializerManager<FermentationRecipe<?>> createRecSerializerManager() {
        return FermentationRecSerializerLegacyManager.getInstance();
    }

}
