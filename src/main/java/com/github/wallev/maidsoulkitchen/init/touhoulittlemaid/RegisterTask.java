package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.wallev.maidsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.task.cook.v1.brewinandchewin.TaskBncKeg;
import com.github.wallev.maidsoulkitchen.task.cook.v1.crokckpot.TaskCpCrockPot;
import com.github.wallev.maidsoulkitchen.task.cook.v1.cuisine.TaskCdCuisineSkillet;
import com.github.wallev.maidsoulkitchen.task.cook.v1.drinkbeer.TaskDbBeerBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.v1.farmersrespite.TaskFrKettle;
import com.github.wallev.maidsoulkitchen.task.cook.v1.farmersdelight.TaskFdCookPot;
import com.github.wallev.maidsoulkitchen.task.cook.v1.farmersdelight.TaskFdCuttingBoard;
import com.github.wallev.maidsoulkitchen.task.cook.v1.kitchencarrot.TaskKkAirCompressor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.kitchencarrot.TaskKkBrewingBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.v1.minecraft.TaskFurnace;
import com.github.wallev.maidsoulkitchen.task.cook.v1.minersdelight.TaskMdCopperPot;
import com.github.wallev.maidsoulkitchen.task.cook.v1.youkaishomecoming.TaskYhcDryingRack;
import com.github.wallev.maidsoulkitchen.task.cook.v1.youkaishomecoming.TaskYhcFermentationTank;
import com.github.wallev.maidsoulkitchen.task.cook.v1.youkaishomecoming.TaskYhcMoka;
import com.github.wallev.maidsoulkitchen.task.cook.v1.youkaishomecoming.TaskYhcTeaKettle;
import com.github.wallev.maidsoulkitchen.task.farm.*;
import com.github.wallev.maidsoulkitchen.task.other.TaskFeedAnimalT;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

public final class RegisterTask {
    private RegisterTask() {
    }

    public static void register(TaskManager manager) {
        if (Mods.MC.isLoaded() && RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskCompatMelonFarm());
        }
        if (Mods.MC.isLoaded() && RegisterConfig.BERRY_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskBerryFarm());
        }
        if (Mods.MC.isLoaded() && RegisterConfig.FRUIT_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskFruitFarm());
        }
        if (Mods.MC.isLoaded() && RegisterConfig.FEED_ANIMAL_T_TASK_ENABLED.get()) {
            manager.add(new TaskFeedAnimalT());
        }

        if (Mods.SS.isLoaded() && RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskSsFarm());
        }
        if (Mods.ES.isLoaded() && RegisterConfig.ECLIPTICSEASONS_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskEsFarm());
        }


        if (Mods.MC.isLoaded() && RegisterConfig.FURNACE_TASK_ENABLED.get()) {
            manager.add(new TaskFurnace());
        }

        if (Mods.FD.isLoaded() && RegisterConfig.FD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(new TaskFdCookPot());
        }
        if (Mods.FD.isLoaded() && RegisterConfig.FD_CUTTING_BOARD_TASK_ENABLED.get()) {
            manager.add(new TaskFdCuttingBoard());
        }
        if (Mods.CD.isLoaded() && RegisterConfig.CD_CUISINE_SKILLET_TASK_ENABLED.get()) {
            manager.add(new TaskCdCuisineSkillet());
        }
        if (Mods.MD.isLoaded() && RegisterConfig.MD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(new TaskMdCopperPot());
        }
        if (Mods.FRD.isLoaded() && RegisterConfig.FR_KETTLE_TASK_ENABLED.get()) {
            manager.add(new TaskFrKettle());
        }
        if (Mods.BNCD.isLoaded() && RegisterConfig.BNC_KEY_TASK_ENABLED.get()) {
            manager.add(new TaskBncKeg());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_MOKA_TASK_ENABLED.get()) {
            manager.add(new TaskYhcMoka());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED.get()) {
            manager.add(new TaskYhcTeaKettle());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED.get()) {
            manager.add(new TaskYhcDryingRack());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED.get()) {
            manager.add(new TaskYhcFermentationTank());
        }


        if (Mods.CP.isLoaded() && RegisterConfig.CP_CROk_POT_TASK_ENABLED.get()) {
            manager.add(new TaskCpCrockPot());
        }
        if (Mods.DB.isLoaded() && RegisterConfig.DB_BEER_TASK_ENABLED.get()) {
            manager.add(new TaskDbBeerBarrel());
        }
        if (Mods.KK.isLoaded() && RegisterConfig.KK_BREW_BARREL.get()) {
            manager.add(new TaskKkBrewingBarrel());
        }
        if (Mods.KK.isLoaded() && RegisterConfig.KK_AIR_COMPRESSOR.get()) {
            manager.add(new TaskKkAirCompressor());
        }
    }
}
