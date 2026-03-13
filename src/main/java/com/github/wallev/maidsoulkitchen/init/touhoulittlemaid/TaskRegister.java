package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.TaskBdBasin;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.TaskBdGrill;
import com.github.wallev.maidsoulkitchen.task.cook.cuisine.TaskCdCuisineSkillet;
import com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.TaskDbBeerBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.TaskFdCookPot;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.TaskFdCuttingBoard;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.TaskFdSkillet;
import com.github.wallev.maidsoulkitchen.task.cook.minecraft.TaskFurnace;
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

    public static void init(TaskManager manager) {
        if (TaskInfo.COMPAT_MELON_FARM.canLoad()) {
            manager.add(new TaskCompatMelonFarm());
        }
        if (TaskInfo.BERRY_FARM.canLoad()) {
            manager.add(new TaskBerryFarm());
        }
        if (TaskInfo.FRUIT_FARM.canLoad()) {
            manager.add(new TaskFruitFarm());
        }
        if (TaskInfo.FEED_ANIMAL_T.canLoad()) {
            manager.add(new TaskFeedAnimalT());
        }

        if (TaskInfo.SERENESEASONS_FARM.canLoad()) {
            manager.add(new TaskSsFarm());
        }
        if (TaskInfo.ECLIPTICSSEASONS_FARM.canLoad()) {
            manager.add(new TaskEsFarm());
        }

        if (TaskInfo.FURNACE.canLoad()) {
            manager.add(new TaskFurnace());
        }

        if (TaskInfo.FD_COOK_POT.canLoad()) {
            manager.add(new TaskFdCookPot());
        }
        if (TaskInfo.FD_CUTTING_BOARD.canLoad()) {
            manager.add(new TaskFdCuttingBoard());
        }
        if (TaskInfo.FD_SKILLET.canLoad()) {
            manager.add(new TaskFdSkillet());
        }
        if (TaskInfo.CD_CUISINE_SKILLET.canLoad()) {
            manager.add(new TaskCdCuisineSkillet());
        }
//        if (Mods.FRD.isLoaded() && RegisterConfig.FR_KETTLE_TASK_ENABLED.get()) {
//            manager.add(new TaskFrKettle());
//        }
        if (TaskInfo.BNC_KEY.canLoad()) {
//            manager.add(new TaskBncKeg());
        }
        if (TaskInfo.BD_BASIN.canLoad()) {
            manager.add(new TaskBdBasin());
        }
        if (TaskInfo.BD_GRILL.canLoad()) {
            manager.add(new TaskBdGrill());
        }
        if (TaskInfo.YHC_MOKA.canLoad()) {
            manager.add(new TaskYhcMoka());
        }
        if (TaskInfo.YHC_TEA_KETTLE.canLoad()) {
            manager.add(new TaskYhcTeaKettle());
        }
        if (TaskInfo.YHC_DRYING_RACK.canLoad()) {
            manager.add(new TaskYhcDryingRack());
        }
        if (TaskInfo.YHC_FERMENTATION_TANK.canLoad()) {
            manager.add(new TaskYhcFermentationTank());
        }

        if (TaskInfo.DB_BEER.canLoad()) {
            manager.add(new TaskDbBeerBarrel());
        }

    }
}
