package com.github.wallev.farmsoulkitchen.init.registry.tlm;

import com.github.wallev.farmsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.farmsoulkitchen.foundation.utility.Mods;
import com.github.wallev.farmsoulkitchen.task.cook.v1.bakery.TaskDbkCookingPot;
//import com.github.wallev.farmsoulkitchen.task.cook.v1.bakery.TaskDclStove;
import com.github.wallev.farmsoulkitchen.task.cook.v1.beachparty.TaskDbpMiniFridge;
import com.github.wallev.farmsoulkitchen.task.cook.v1.beachparty.TaskDbpTikiBar;
import com.github.wallev.farmsoulkitchen.task.cook.v1.bnc.TaskBncKey;
//import com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight.TaskDclCookingPan;
//import com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight.TaskDclCookingPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight.TaskDclCookingPan;
import com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight.TaskDclCookingPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight.TaskDclStove;
import com.github.wallev.farmsoulkitchen.task.cook.v1.crokckpot.TaskCrockPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.drinkbeer.TaskDbBeerBarrel;
import com.github.wallev.farmsoulkitchen.task.cook.v1.farmacharm.TaskDfcCookingPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.farmacharm.TaskDfcRoast;
import com.github.wallev.farmsoulkitchen.task.cook.v1.farmacharm.TaskDfcStove;
import com.github.wallev.farmsoulkitchen.task.cook.v1.fd.TaskFDCookPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.fd.TaskFdCuttingBoard;
import com.github.wallev.farmsoulkitchen.task.cook.v1.herbal.TaskDhbCauldron;
import com.github.wallev.farmsoulkitchen.task.cook.v1.herbal.TaskDhbTeaKettle;
import com.github.wallev.farmsoulkitchen.task.cook.v1.kitchencarrot.TaskAirCompressor;
import com.github.wallev.farmsoulkitchen.task.cook.v1.kitchencarrot.TaskKkBrewingBarrel;
import com.github.wallev.farmsoulkitchen.task.cook.v1.mc.TaskFurnace;
import com.github.wallev.farmsoulkitchen.task.cook.v1.md.TaskMDCopperPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.vinery.TaskFermentationBarrel;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskDryingRack;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskFermentationTank;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskYhcMoka;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskYhcTeaKettle;
import com.github.wallev.farmsoulkitchen.task.farm.TaskBerryFarm;
import com.github.wallev.farmsoulkitchen.task.farm.TaskCompatMelonFarm;
import com.github.wallev.farmsoulkitchen.task.farm.TaskFruitFarm;
import com.github.wallev.farmsoulkitchen.task.farm.TaskSSFarm;
import com.github.wallev.farmsoulkitchen.task.other.TaskFeedAndDrinkOwner;
import com.github.wallev.farmsoulkitchen.task.other.TaskFeedAnimalT;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

public final class RegisterTask {
    private RegisterTask() {
    }

    public static void register(TaskManager manager) {
        if (Mods.MC.isLoaded() && RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED.get()){
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
            manager.add(new TaskSSFarm());
        }


        if (Mods.TWT.isLoaded() && RegisterConfig.FEED_AND_DRINK_OWNER_TASK_ENABLED.get()) {
            manager.add(new TaskFeedAndDrinkOwner());
        }


        if (Mods.MC.isLoaded() && RegisterConfig.FURNACE_TASK_ENABLED.get()) {
            manager.add(new TaskFurnace());
        }


        if (Mods.FD.isLoaded() && RegisterConfig.FD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(new TaskFDCookPot());
        }
        if (Mods.FD.isLoaded() && RegisterConfig.FD_CUTTING_BOARD_TASK_ENABLED.get()) {
            manager.add(new TaskFdCuttingBoard());
        }
        if (Mods.MD.isLoaded() && RegisterConfig.MD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(new TaskMDCopperPot());
        }
        if (Mods.BNCD.isLoaded() && RegisterConfig.BNC_KEY_TASK_ENABLED.get()) {
            manager.add(new TaskBncKey());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_MOKA_TASK_ENABLED.get()) {
            manager.add(new TaskYhcMoka());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED.get()) {
            manager.add(new TaskYhcTeaKettle());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED.get()) {
            manager.add(new TaskDryingRack());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED.get()) {
            manager.add(new TaskFermentationTank());
        }


        if (Mods.CP.isLoaded() && RegisterConfig.CP_CROk_POT_TASK_ENABLED.get()) {
            manager.add(new TaskCrockPot());
        }
        if (Mods.DB.isLoaded() && RegisterConfig.DB_BEER_TASK_ENABLED.get()) {
            manager.add(new TaskDbBeerBarrel());
        }
        if (Mods.KK.isLoaded() && RegisterConfig.KK_BREW_BARREL.get()) {
            manager.add(new TaskKkBrewingBarrel());
        }
        if (Mods.KK.isLoaded() && RegisterConfig.KK_AIR_COMPRESSOR.get()) {
            manager.add(new TaskAirCompressor());
        }


        if (Mods.DBK.isLoaded() && RegisterConfig.DBK_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(new TaskDbkCookingPot());
        }

        if (Mods.DBP.isLoaded() && RegisterConfig.DBP_MINE_FRIDGE_TASK_ENABLED.get()) {
            manager.add(new TaskDbpMiniFridge());
        }
        if (Mods.DBP.isLoaded() && RegisterConfig.DBP_TIKI_BAR_TASK_ENABLED.get()) {
            manager.add(new TaskDbpTikiBar());
        }

        if (Mods.DCL.isLoaded() && RegisterConfig.DCL_COOKING_PAN_TASK_ENABLED.get()) {
            manager.add(new TaskDclCookingPan());
        }
        if (Mods.DCL.isLoaded() && RegisterConfig.DCL_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(new TaskDclCookingPot());
        }
        if (Mods.DCL.isLoaded() && RegisterConfig.DCL_STOVE_TASK_ENABLED.get()) {
            manager.add(new TaskDclStove());
        }

        if (Mods.DFC.isLoaded() && RegisterConfig.DFC_ROAST_TASK_ENABLED.get()) {
            manager.add(new TaskDfcRoast());
        }
        if (Mods.DFC.isLoaded() && RegisterConfig.DFC_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(new TaskDfcCookingPot());
        }
        if (Mods.DFC.isLoaded() && RegisterConfig.DFC_STOVE_TASK_ENABLED.get()) {
            manager.add(new TaskDfcStove());
        }

        if (Mods.DHB.isLoaded() && RegisterConfig.DHB_CAULDRON_TASK_ENABLED.get()) {
            manager.add(new TaskDhbCauldron());
        }
        if (Mods.DHB.isLoaded() && RegisterConfig.DHB_TEA_KETTLE_TASK_ENABLED.get()) {
            manager.add(new TaskDhbTeaKettle());
        }

        if (Mods.DV.isLoaded() && RegisterConfig.FERMENTATION_BARREL_TASK_ENABLED.get()) {
            manager.add(new TaskFermentationBarrel());
        }
    }
}
