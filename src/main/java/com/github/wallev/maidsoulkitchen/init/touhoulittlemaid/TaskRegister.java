package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.wallev.maidsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.task.cook.bakery.TaskDbkCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.TaskBdBasin;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.TaskBdGrill;
import com.github.wallev.maidsoulkitchen.task.cook.beachparty.TaskDbpMiniFridge;
import com.github.wallev.maidsoulkitchen.task.cook.beachparty.TaskDbpTikiBar;
import com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.TaskBncKeg;
import com.github.wallev.maidsoulkitchen.task.cook.candlelight.TaskDclCookingPan;
import com.github.wallev.maidsoulkitchen.task.cook.candlelight.TaskDclCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.candlelight.TaskDclStove;
import com.github.wallev.maidsoulkitchen.task.cook.crokckpot.TaskCpCrockPot;
import com.github.wallev.maidsoulkitchen.task.cook.cuisine.TaskCdCuisineSkillet;
import com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.TaskDbBeerBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.farmacharm.TaskDfcCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.farmacharm.TaskDfcRoast;
import com.github.wallev.maidsoulkitchen.task.cook.farmacharm.TaskDfcStove;
import com.github.wallev.maidsoulkitchen.task.cook.farmersrespite.TaskFrKettle;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.TaskFdCookPot;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.TaskFdCuttingBoard;
import com.github.wallev.maidsoulkitchen.task.cook.herbalbrews.TaskDhbCauldron;
import com.github.wallev.maidsoulkitchen.task.cook.herbalbrews.TaskDhbTeaKettle;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.TaskKkAirCompressor;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.TaskKkBrewingBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.minecraft.TaskFurnace;
import com.github.wallev.maidsoulkitchen.task.cook.minersdelight.TaskMdCopperPot;
import com.github.wallev.maidsoulkitchen.task.cook.vinery.TaskFermentationBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.TaskYhcDryingRack;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.TaskYhcFermentationTank;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.TaskYhcMoka;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.TaskYhcTeaKettle;
import com.github.wallev.maidsoulkitchen.task.farm.*;
import com.github.wallev.maidsoulkitchen.task.other.TaskFeedAnimalT;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

public final class TaskRegister {
    private TaskRegister() {
    }

    public static void init(TaskManager task) {
        if (Mods.MC.isLoaded && RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED.get()) {
            task.add(new TaskCompatMelonFarm());
        }
        if (Mods.MC.isLoaded && RegisterConfig.BERRY_FARM_TASK_ENABLED.get()) {
            task.add(new TaskBerryFarm());
        }
        if (Mods.MC.isLoaded && RegisterConfig.FRUIT_FARM_TASK_ENABLED.get()) {
            task.add(new TaskFruitFarm());
        }
        if (Mods.MC.isLoaded && RegisterConfig.FEED_ANIMAL_T_TASK_ENABLED.get()) {
            task.add(new TaskFeedAnimalT());
        }

        if (Mods.SS.isLoaded && RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED.get()) {
            task.add(new TaskSsFarm());
        }
        if (Mods.ES.isLoaded && RegisterConfig.ECLIPTICSEASONS_FARM_TASK_ENABLED.get()) {
            task.add(new TaskEsFarm());
        }


        if (Mods.MC.isLoaded && RegisterConfig.FURNACE_TASK_ENABLED.get()) {
            task.add(new TaskFurnace());
        }

        if (Mods.FD.isLoaded && RegisterConfig.FD_COOK_POT_TASK_ENABLED.get()) {
            task.add(new TaskFdCookPot());
        }
        if (Mods.FD.isLoaded && RegisterConfig.FD_CUTTING_BOARD_TASK_ENABLED.get()) {
            task.add(new TaskFdCuttingBoard());
        }
        if (Mods.CD.isLoaded && RegisterConfig.CD_CUISINE_SKILLET_TASK_ENABLED.get()) {
            task.add(new TaskCdCuisineSkillet());
        }
        if (Mods.MD.isLoaded && RegisterConfig.MD_COOK_POT_TASK_ENABLED.get()) {
            task.add(new TaskMdCopperPot());
        }
        if (Mods.FRD.isLoaded && RegisterConfig.FR_KETTLE_TASK_ENABLED.get()) {
            task.add(new TaskFrKettle());
        }
        if (Mods.BNCD.isLoaded && RegisterConfig.BNC_KEY_TASK_ENABLED.get()) {
            task.add(new TaskBncKeg());
        }
        if (Mods.BD.isLoaded && RegisterConfig.BD_BASIN_TASK_ENABLED.get()) {
            task.add(new TaskBdBasin());
        }
        if (Mods.BD.isLoaded && RegisterConfig.BD_GRILL_TASK_ENABLED.get()) {
            task.add(new TaskBdGrill());
        }
        if (Mods.YHCD.isLoaded && RegisterConfig.YHC_MOKA_TASK_ENABLED.get()) {
            task.add(new TaskYhcMoka());
        }
        if (Mods.YHCD.isLoaded && RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED.get()) {
            task.add(new TaskYhcTeaKettle());
        }
        if (Mods.YHCD.isLoaded && RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED.get()) {
            task.add(new TaskYhcDryingRack());
        }
        if (Mods.YHCD.isLoaded && RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED.get()) {
            task.add(new TaskYhcFermentationTank());
        }


        if (Mods.CP.isLoaded && RegisterConfig.CP_CROk_POT_TASK_ENABLED.get()) {
            task.add(new TaskCpCrockPot());
        }
        if (Mods.DB.isLoaded && RegisterConfig.DB_BEER_TASK_ENABLED.get()) {
            task.add(new TaskDbBeerBarrel());
        }
        if (Mods.KK.isLoaded && RegisterConfig.KK_BREW_BARREL.get()) {
            task.add(new TaskKkBrewingBarrel());
        }
        if (Mods.KK.isLoaded && RegisterConfig.KK_AIR_COMPRESSOR.get()) {
            task.add(new TaskKkAirCompressor());
        }

        if (Mods.DBK.isLoaded && RegisterConfig.DBK_COOKING_POT_TASK_ENABLED.get()) {
            task.add(new TaskDbkCookingPot());
        }

        if (Mods.DBP.isLoaded && RegisterConfig.DBP_MINE_FRIDGE_TASK_ENABLED.get()) {
            task.add(new TaskDbpMiniFridge());
        }
        if (Mods.DBP.isLoaded && RegisterConfig.DBP_TIKI_BAR_TASK_ENABLED.get()) {
            task.add(new TaskDbpTikiBar());
        }

        if (Mods.DCL.isLoaded && RegisterConfig.DCL_COOKING_PAN_TASK_ENABLED.get()) {
            task.add(new TaskDclCookingPan());
        }
        if (Mods.DCL.isLoaded && RegisterConfig.DCL_COOKING_POT_TASK_ENABLED.get()) {
            task.add(new TaskDclCookingPot());
        }
        if (Mods.DCL.isLoaded && RegisterConfig.DCL_STOVE_TASK_ENABLED.get()) {
            task.add(new TaskDclStove());
        }

        if (Mods.DFC.isLoaded && RegisterConfig.DFC_ROAST_TASK_ENABLED.get()) {
            task.add(new TaskDfcRoast());
        }
        if (Mods.DFC.isLoaded && RegisterConfig.DFC_COOKING_POT_TASK_ENABLED.get()) {
            task.add(new TaskDfcCookingPot());
        }
        if (Mods.DFC.isLoaded && RegisterConfig.DFC_STOVE_TASK_ENABLED.get()) {
            task.add(new TaskDfcStove());
        }

        if (Mods.DHB.isLoaded && RegisterConfig.DHB_CAULDRON_TASK_ENABLED.get()) {
            task.add(new TaskDhbCauldron());
        }
        if (Mods.DHB.isLoaded && RegisterConfig.DHB_TEA_KETTLE_TASK_ENABLED.get()) {
            task.add(new TaskDhbTeaKettle());
        }

        if (Mods.DV.isLoaded && RegisterConfig.FERMENTATION_BARREL_TASK_ENABLED.get()) {
            task.add(new TaskFermentationBarrel());
        }
    }
}
