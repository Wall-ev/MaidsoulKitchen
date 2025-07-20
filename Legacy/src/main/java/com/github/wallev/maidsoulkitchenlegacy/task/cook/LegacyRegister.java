package com.github.wallev.maidsoulkitchenlegacy.task.cook;

import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchenlegacy.task.cook.kitchenkarrot.brewing.TaskKkBrewingBarrelLegacy;
import com.github.wallev.maidsoulkitchenlegacy.task.cook.youkaishomecoming.ferment.TaskYhcFermentationTankLegacy;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class LegacyRegister {

    public static void register() {
        LegacyTaskInfo.init();
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
        Supplier<ResourceLocation> resourceLocationSupplier = () -> {
            return legacyTaskInfo.uid;
        };
        Supplier<Mods> modsSupplier = () -> {
            return legacyTaskInfo.bindMod;
        };
        Supplier<Boolean> booleanSupplier = () -> {
            return legacyTaskInfo.configEnable.get();
        };
        Supplier<ICookTask<?, ?>> taskSupplier = () -> task.get();
        TaskRegister.addLegacyCookTask(
                resourceLocationSupplier,
                modsSupplier,
                booleanSupplier,
                taskSupplier,
                legacyTaskInfo.mixinList);
    }
}
