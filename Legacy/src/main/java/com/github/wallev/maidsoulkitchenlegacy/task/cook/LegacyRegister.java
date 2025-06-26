package com.github.wallev.maidsoulkitchenlegacy.task.cook;

import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchenlegacy.task.cook.kitchenkarrot.brewing.TaskKkBrewingBarrelLegacy;
import com.github.wallev.maidsoulkitchenlegacy.task.cook.youkaishomecoming.ferment.TaskYhcFermentationTankLegacy;

import java.util.function.Supplier;

public class LegacyRegister {

    public static void register() {
        addLegacyCompat();
    }

    private static void addLegacyCompat() {
//        addLegacyTask(LegacyTaskInfo.YHC_FERMENTITAION, () -> {
//            return new TaskYhcFermentationTankLegacy();
//        });
//        addLegacyTask(LegacyTaskInfo.KK_BREWING, () -> {
//            return new TaskKkBrewingBarrelLegacy();
//        });
    }

    private static void addLegacyTask(LegacyTaskInfo legacyTaskInfo, Supplier<IMaidsoulKitchenTask> task) {
        TaskRegister.addLegacyTask(
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
