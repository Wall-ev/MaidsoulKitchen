package com.github.wallev.maidsoulkitchen.task;

import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.cook.baker.cookingpot.TaskDbSmallCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.basin.TaskBbqBasin;
import com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill.TaskBbqGrill;
import com.github.wallev.maidsoulkitchen.task.cook.beachparty.minifridge.TaskDbpMiniFridge;
import com.github.wallev.maidsoulkitchen.task.cook.beachparty.palmbar.TaskDbpPalmBar;
import com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg.TaskBncKeg;
import com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpan.TaskDclCookingPan;
import com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpot.TaskDclCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.copperpot.cooking.TaskCpCopperPot;
import com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot.TaskCpCrockPot;
import com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine.TaskCdCuisine;
import com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.beerbarrel.TaskDbBeerBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.dungeonsdelight.cooking.TaskDdMonsterPot;
import com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.cookingpot.TaskDfcCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.roaster.TaskDfcRoaster;
import com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.stove.TaskDfcStove;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cookingpot.TaskFdCookingPot;
import com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cuttingboard.TaskFdCuttingBoard;
import com.github.wallev.maidsoulkitchen.task.cook.farmersrespite.kettle.TaskFrKettle;
import com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.choppingboard.TaskKcChoppingBoard;
import com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery.TaskKcPot;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.aircompressor.TaskKkAirCompressor;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.brewing.TaskKkBrewingBarrel;
import com.github.wallev.maidsoulkitchen.task.cook.meadow.cheeseform.TaskDmCheeseForm;
import com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace.TaskFurnace;
import com.github.wallev.maidsoulkitchen.task.cook.minersdelight.cooking.TaskMdCopperPot;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.dryingrack.TaskYhcDryingRack;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.ferment.TaskYhcFermentationTank;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.kettle.TaskYhcKettle;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.moka.TaskYhcMoka;
import com.github.wallev.maidsoulkitchen.util.classana.TaskModClazzManager;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public enum CookTask {

    FURNACE(TaskInfo.FURNACE, TaskFurnace::new),

    KC_POT(TaskInfo.KC_POT, TaskKcPot::new),
    KC_CHOPPING_BOARD(TaskInfo.KC_CHOPPING_BOARD, TaskKcChoppingBoard::new),

    FD_COOK_POT(TaskInfo.FD_COOK_POT, TaskFdCookingPot::new),
    FD_CUTTING_BOARD(TaskInfo.FD_CUTTING_BOARD, TaskFdCuttingBoard::new),

    CD_CUISINE_SKILLET(TaskInfo.CD_CUISINE_SKILLET, TaskCdCuisine::new),

    MD_COOK_POT(TaskInfo.MD_COOK_POT, TaskMdCopperPot::new),

    CP_COPPER_POT(TaskInfo.COPPER_POT, TaskCpCopperPot::new),

    DD_MONSTER_POT(TaskInfo.MONSTER_POT, TaskDdMonsterPot::new),

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

    DFC_STOVE(TaskInfo.DFC_STOVE, TaskDfcStove::new),
    DFC_ROASTER(TaskInfo.DFC_ROASTER, TaskDfcRoaster::new),
    DFC_COOKING_POT(TaskInfo.DFC_COOKING_POT, TaskDfcCookingPot::new),

    DBK_COOKING_POT(TaskInfo.DBK_COOKING_POT, TaskDbSmallCookingPot::new),

    DCL_COOKING_POT(TaskInfo.DCL_COOKING_POT, TaskDclCookingPot::new),
    DCL_COOKING_PAN(TaskInfo.DCL_COOKING_PAN, TaskDclCookingPan::new),

    DBP_MINI_FRIDGE(TaskInfo.DBP_MINI_FRIDGE, TaskDbpMiniFridge::new),
    DBP_PALM_BAR(TaskInfo.DBP_PALM_BAR, TaskDbpPalmBar::new),

    DM_CHEESE_FORM(TaskInfo.DM_CHEESE_FORM, TaskDmCheeseForm::new),

    ;
    public final ResourceLocation uid;
    public final String modId;
    public final Supplier<Boolean> canLoad;
    public final Supplier<ICookTask<?, ?>> bindTask;

    CookTask(TaskInfo taskInfo, Supplier<ICookTask<?, ?>> bindTask) {
        this.uid = taskInfo.getUid();
        this.modId = taskInfo.getBindMod().modId;
        this.canLoad = () -> {
            return taskInfo.modVersionLoaded() && taskInfo.configEnabled() && TaskModClazzManager.clazzLoad(this.uid);
        };
        this.bindTask = bindTask;
    }

    public static void init() {
    }

}
