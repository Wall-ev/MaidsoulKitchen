package com.github.wallev.maidsoulkitchenlegacy.task.cook;

import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchenlegacy.task.cook.kitchenkarrot.brewing.TaskKkBrewingBarrelLegacy;
import com.github.wallev.maidsoulkitchenlegacy.task.cook.youkaishomecoming.ferment.TaskYhcFermentationTankLegacy;

import java.util.function.Supplier;

public class LegacyRegister {

    public static void register() {
        addLegacyCompat();
    }

    private static void addLegacyCompat() {
        addLegacyCookTask(LegacyTaskInfo.YHC_FERMENTITAION, () -> {
            return new TaskYhcFermentationTankLegacy();
        });
        addLegacyCookTask(LegacyTaskInfo.KK_BREWING, () -> {
            return new TaskKkBrewingBarrelLegacy();
        });
    }

    private static void addLegacyCookTask(LegacyTaskInfo legacyTaskInfo, Supplier<ICookTask<?, ?>> task) {
        TaskRegister.addLegacyCookTask(
                () -> {
                    return legacyTaskInfo.uid;
                },
                () -> {
                    return legacyTaskInfo.bindMod;
                },
                () -> {
                    return legacyTaskInfo.bindConfig.get();
                },
                task,
                legacyTaskInfo.mixinList);
    }
}
