package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;

public final class TaskRegister {
    private TaskRegister() {
    }

    public static void init(TaskManager manager) {
        IMaidsoulKitchenTask.getTasks().forEach((canAdd, task) -> {
            if (canAdd.get()) {
                IMaidsoulKitchenTask maidsoulKitchenTask = task.get();
                manager.add(maidsoulKitchenTask);
                if (maidsoulKitchenTask instanceof ICookTask<?,?> cookTask) {
                    ICookTask.putTask(cookTask.getUid(), cookTask);
                }
            }
        });

        /**
         *  if (Mods.MC.isLoaded && RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED.get()) {
         *             manager.add(new TaskCompatMelonFarm());
         *         }
         *         if (Mods.MC.isLoaded && RegisterConfig.BERRY_FARM_TASK_ENABLED.get()) {
         *             manager.add(new TaskBerryFarm());
         *         }
         *         if (Mods.MC.isLoaded && RegisterConfig.FRUIT_FARM_TASK_ENABLED.get()) {
         *             manager.add(new TaskFruitFarm());
         *         }
         *         if (Mods.MC.isLoaded && RegisterConfig.FEED_ANIMAL_T_TASK_ENABLED.get()) {
         *             manager.add(new TaskFeedAnimalT());
         *         }
         *
         *         if (Mods.SS.isLoaded && RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED.get()) {
         *             manager.add(new TaskSsFarm());
         *         }
         *         if (Mods.ES.isLoaded && RegisterConfig.ECLIPTICSEASONS_FARM_TASK_ENABLED.get()) {
         *             manager.add(new TaskEsFarm());
         *         }
         *
         *
         *         if (Mods.MC.isLoaded && RegisterConfig.FURNACE_TASK_ENABLED.get()) {
         *             manager.add(new TaskFurnace());
         *         }
         *         manager.add(new TaskKcPot());
         *         manager.add(new TaskKcChoppingBoard());
         *         if (Mods.FD.isLoaded && RegisterConfig.FD_COOK_POT_TASK_ENABLED.get()) {
         *             manager.add(new TaskFdCookingPot());
         *         }
         *         if (Mods.FD.isLoaded && RegisterConfig.FD_CUTTING_BOARD_TASK_ENABLED.get()) {
         *             manager.add(new TaskFdCuttingBoard());
         *         }
         *         if (Mods.CD.isLoaded && RegisterConfig.CD_CUISINE_SKILLET_TASK_ENABLED.get()) {
         *             manager.add(new TaskCdCuisine());
         *         }
         *         if (Mods.MD.isLoaded && RegisterConfig.MD_COOK_POT_TASK_ENABLED.get()) {
         *             manager.add(new TaskMdCopperPot());
         *         }
         *         if (Mods.FRD.isLoaded && RegisterConfig.FR_KETTLE_TASK_ENABLED.get()) {
         *             manager.add(new TaskFrKettle());
         *         }
         *         if (Mods.BNCD.isLoaded && RegisterConfig.BNC_KEY_TASK_ENABLED.get()) {
         *             manager.add(new TaskBncKeg());
         *         }
         *         if (Mods.BD.isLoaded && RegisterConfig.BD_BASIN_TASK_ENABLED.get()) {
         *             manager.add(new TaskBbqBasin());
         *         }
         *         if (Mods.BD.isLoaded && RegisterConfig.BD_GRILL_TASK_ENABLED.get()) {
         *             manager.add(new TaskBbqGrill());
         *         }
         *         if (Mods.YHCD.isLoaded && RegisterConfig.YHC_MOKA_TASK_ENABLED.get()) {
         *             manager.add(new TaskYhcMoka());
         *         }
         *         if (Mods.YHCD.isLoaded && RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED.get()) {
         *             manager.add(new TaskYhcKettle());
         *         }
         *         if (Mods.YHCD.isLoaded && RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED.get()) {
         *             manager.add(new TaskYhcDryingRack());
         *         }
         *         if (Mods.YHCD.isLoaded && RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED.get()) {
         *             manager.add(new TaskYhcFermentationTank());
         *         }
         *         if (Mods.CP.isLoaded && RegisterConfig.CP_CROk_POT_TASK_ENABLED.get()) {
         *             manager.add(new TaskCpCrockPot());
         *         }
         *         if (Mods.DB.isLoaded && RegisterConfig.DB_BEER_TASK_ENABLED.get()) {
         *             manager.add(new TaskDbBeerBarrel());
         *         }
         *         if (Mods.KK.isLoaded && RegisterConfig.KK_BREW_BARREL.get()) {
         *             manager.add(new TaskKkBrewingBarrel());
         *         }
         *         if (Mods.KK.isLoaded && RegisterConfig.KK_AIR_COMPRESSOR.get()) {
         *             manager.add(new TaskKkAirCompressor());
         *         }
         */
    }
}
