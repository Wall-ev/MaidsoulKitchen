package com.github.wallev.maidsoulkitchen.task;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.basin.TaskBbqBasin;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill.TaskBbqGrill;
import com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg.TaskBncKeg;
import com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot.TaskCpCrockPot;
import com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine.TaskCdCuisine;
import com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.beerbarrel.TaskDbBeerBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cookingpot.TaskFdCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cuttingboard.TaskFdCuttingBoard;
import com.github.wallev.maidsoulkitchen.task.cook.farmersrespite.kettle.TaskFrKettle;
import com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.choppingboard.TaskKcChoppingBoard;
import com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery.TaskKcPot;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.aircompressor.TaskKkAirCompressor;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.brewing.TaskKkBrewingBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace.TaskFurnace;
import com.github.wallev.maidsoulkitchen.task.cook.minersdelight.cooking.TaskMdCopperPot;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.dryingrack.TaskYhcDryingRack;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.ferment.TaskYhcFermentationTank;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.kettle.TaskYhcKettle;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.moka.TaskYhcMoka;
import com.github.wallev.maidsoulkitchen.task.farm.*;
import com.github.wallev.maidsoulkitchen.task.other.TaskFeedAnimalT;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixinManager;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public enum MaidsoulKitchenTask {
    COMPAT_MELON_FARM(TaskInfo.COMPAT_MELON_FARM, TaskCompatMelonFarm::new),
    BERRY_FARM(TaskInfo.BERRY_FARM, TaskBerryFarm::new),
    FRUIT_FARM(TaskInfo.FRUIT_FARM, TaskFruitFarm::new),

    FEED_ANIMAL_T(TaskInfo.FEED_ANIMAL_T, TaskFeedAnimalT::new),

    SERENESEASONS_FARM(TaskInfo.SERENESEASONS_FARM, TaskSsFarm::new),

    ECLIPTICSSEASONS_FARM(TaskInfo.ECLIPTICSSEASONS_FARM, TaskEsFarm::new),

    FURNACE(TaskInfo.FURNACE, TaskFurnace::new),

    KC_POT(TaskInfo.KC_POT, TaskKcPot::new),
    KC_CHOPPING_BOARD(TaskInfo.KC_CHOPPING_BOARD, TaskKcChoppingBoard::new),

    FD_COOK_POT(TaskInfo.FD_COOK_POT, TaskFdCookingPot::new),
    FD_CUTTING_BOARD(TaskInfo.FD_CUTTING_BOARD, TaskFdCuttingBoard::new),

    CD_CUISINE_SKILLET(TaskInfo.CD_CUISINE_SKILLET, TaskCdCuisine::new),

    MD_COOK_POT(TaskInfo.MD_COOK_POT, TaskMdCopperPot::new),

    BNC_KEY(TaskInfo.BNC_KEY, TaskBncKeg::new),

    FR_KETTLE(TaskInfo.FR_KETTLE, TaskFrKettle::new),

    BD_BASIN(TaskInfo.BD_BASIN, TaskBbqBasin::new),
    BD_GRILL(TaskInfo.BD_GRILL, TaskBbqGrill::new),

    YHC_MOKA(TaskInfo.YHC_MOKA, TaskYhcMoka::new),
    YHC_TEA_KETTLE(TaskInfo.YHC_TEA_KETTLE, TaskYhcKettle::new),
    YHC_DRYING_RACK(TaskInfo.YHC_DRYING_RACK, TaskYhcDryingRack::new),
    YHC_FERMENTATION_TANK(TaskInfo.YHC_FERMENTATION_TANK, TaskYhcFermentationTank::new),

    KK_BREW_BARREL(TaskInfo.KK_BREW_BARREL, TaskKkBrewingBarrel::new),
    KK_AIR_COMPRESSOR(TaskInfo.KK_AIR_COMPRESSOR, TaskKkAirCompressor::new),

    DB_BEER(TaskInfo.DB_BEER, TaskDbBeerBarrel::new),
    CP_CROCK_POT(TaskInfo.CP_CROCK_POT, TaskCpCrockPot::new),

    ;
    public final ResourceLocation uid;

    /**
     * @param uid 任务ID标识符
     * @param bindMod 对应的模组信息
     * @param bindConfig 对应的配置
     * @param bindTask 对应的任务
     */
    MaidsoulKitchenTask(String uid, Mods bindMod, ForgeConfigSpec.BooleanValue bindConfig, Supplier<IMaidsoulKitchenTask> bindTask) {
        this.uid = VResourceLocation.create(MaidsoulKitchen.MOD_ID, uid);
        IMaidsoulKitchenTask.putTask(this.uid, () -> {
            return bindMod.versionLoaded && bindConfig.get() && TaskMixinManager.isApplyMixin(this.uid) && TaskRegister.clazzLoad(this.uid);
        }, bindTask);
    }

    MaidsoulKitchenTask(TaskInfo taskInfo, Supplier<IMaidsoulKitchenTask> bindTask) {
        this.uid = taskInfo.uid;
        IMaidsoulKitchenTask.putTask(this.uid, () -> {
            return taskInfo.bindMod.versionLoaded && taskInfo.bindConfig.get().get() && TaskMixinManager.isApplyMixin(this.uid) && TaskRegister.clazzLoad(this.uid);
        }, bindTask);
    }

    public static void init() {
    }
}
