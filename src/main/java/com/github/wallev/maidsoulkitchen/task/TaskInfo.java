package com.github.wallev.maidsoulkitchen.task;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.config.subconfig.RegisterConfig;
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
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public enum TaskInfo {
    COMPAT_MELON_FARM("compat_melon",
            Mods.MC,
            RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED,
            TaskCompatMelonFarm::new),
    BERRY_FARM("berries_farm",
            Mods.MC,
            RegisterConfig.BERRY_FARM_TASK_ENABLED,
            TaskBerryFarm::new),
    FRUIT_FARM("fruit_farm",
            Mods.MC,
            RegisterConfig.FRUIT_FARM_TASK_ENABLED,
            TaskFruitFarm::new),

    FEED_ANIMAL_T("feed_animal_t",
            Mods.MC,
            RegisterConfig.FEED_ANIMAL_T_TASK_ENABLED,
            TaskFeedAnimalT::new),

    SERENESEASONS_FARM("sereneseasons_farm",
            Mods.SS,
            RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED,
            TaskSsFarm::new),

    ECLIPTICSSEASONS_FARM("eclipticseasons_farm",
            Mods.ES,
            RegisterConfig.ECLIPTICSEASONS_FARM_TASK_ENABLED,
            TaskEsFarm::new),

    FURNACE("furnace",
            Mods.MC,
            RegisterConfig.FURNACE_TASK_ENABLED,
            TaskFurnace::new,
            "net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity"),

    KC_POT("kc_pot",
            Mods.KC,
            RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED,
            TaskKcPot::new),
    KC_CHOPPING_BOARD("kc_chopping_board",
            Mods.KC,
            RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED,
            TaskKcChoppingBoard::new),

    FD_COOK_POT("fd_cooking_pot",
            Mods.FD,
            RegisterConfig.FD_COOK_POT_TASK_ENABLED,
            TaskFdCookingPot::new,
            "vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity"),
    FD_CUTTING_BOARD("fd_cutting_board",
            Mods.FD,
            RegisterConfig.FD_CUTTING_BOARD_TASK_ENABLED,
            TaskFdCuttingBoard::new),

    CD_CUISINE_SKILLET("cd_cuisine_skillet",
            Mods.CD,
            RegisterConfig.CD_CUISINE_SKILLET_TASK_ENABLED,
            TaskCdCuisine::new,
            "dev.xkmc.cuisinedelight.content.logic.CookingData",
            "dev.xkmc.cuisinedelight.content.logic.CookingData$CookingEntry"),

    MD_COOK_POT("md_copper_pot",
            Mods.MD,
            RegisterConfig.MD_COOK_POT_TASK_ENABLED,
            TaskMdCopperPot::new,
            "com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity"),

    BNC_KEY("bnc_key",
            Mods.BNCD,
            RegisterConfig.BNC_KEY_TASK_ENABLED,
            TaskBncKeg::new,
            "umpaz.brewinandchewin.common.block.entity.KegBlockEntity"),

    FR_KETTLE("fr_kettle",
            Mods.FRD,
            RegisterConfig.FR_KETTLE_TASK_ENABLED,
            TaskFrKettle::new,
            "umpaz.farmersrespite.common.block.entity.KettleBlockEntity"),

    BD_BASIN("bd_basin",
            Mods.BD,
            RegisterConfig.BD_BASIN_TASK_ENABLED,
            TaskBbqBasin::new),
    BD_GRILL("bd_grill",
            Mods.BD,
            RegisterConfig.BD_GRILL_TASK_ENABLED,
            TaskBbqGrill::new),

    YHC_MOKA("yhc_moka_pot",
            Mods.YHCD,
            RegisterConfig.YHC_MOKA_TASK_ENABLED,
            TaskYhcMoka::new,
            "dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity"),
    YHC_TEA_KETTLE("yhc_tea_kettle",
            Mods.YHCD,
            RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED,
            TaskYhcKettle::new,
            "dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity",
            "dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlock"),
    YHC_DRYING_RACK("yhc_drying_rack",
            Mods.YHCD,
            RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED,
            TaskYhcDryingRack::new),
    YHC_FERMENTATION_TANK("yhc_fermentation_tank",
            Mods.YHCD,
            RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED,
            TaskYhcFermentationTank::new),

    KK_BREW_BARREL("kk_brew_barrel",
            Mods.KK,
            RegisterConfig.KK_BREW_BARREL,
            TaskKkBrewingBarrel::new,
            "io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity"),
    KK_AIR_COMPRESSOR("kk_air_compressor",
            Mods.KK,
            RegisterConfig.KK_AIR_COMPRESSOR,
            TaskKkAirCompressor::new,
            "io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity"),

    DB_BEER("drinkbeer_beerbarrel",
            Mods.DB,
            RegisterConfig.DB_BEER_TASK_ENABLED,
            TaskDbBeerBarrel::new,
            "lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity"),
    CP_CROCK_POT("cp_crock_pot",
            Mods.CP,
            RegisterConfig.CP_CROk_POT_TASK_ENABLED,
            TaskCpCrockPot::new),

//    DBK_COOKING_POT("dkb_cooking_pot", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DBP_MINE_FRIDGE("dbp_mini_fridge", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DBP_TIKI_BAR("dbp_tiki_bar", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DCL_COOKING_PAN("dcl_cooking_pan", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DCL_COOKING_POT("dcl_cooking_pot",, Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DCL_STOVE("dcl_stove", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DFC_ROAST("dfc_roast", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DFC_COOKING_POT("dfc_cooking_pot", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DFC_STOVE("dfc_stove", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DHB_CAULDRON("dhb_cauldron", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    DHB_TEA_KETTLE("dhb_tea_kettle", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),
//    FERMENTATION_BARREL("dv_fermentation_barrel", Mods.MC, RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED, TaskCompatMelonFarm::new),

    ;
    public final ResourceLocation uid;

    /**
     * @param uid 任务ID标识符
     * @param bindMod 对应的模组信息
     * @param bindConfig 对应的配置
     * @param bindTask 对应的任务
     * @param mixinClz 任务所需mixin的目标类的路径
     */
    TaskInfo(String uid, Mods bindMod, ForgeConfigSpec.BooleanValue bindConfig, Supplier<IMaidsoulKitchenTask> bindTask, String... mixinClz) {
        this.uid = VResourceLocation.create(MaidsoulKitchen.MOD_ID, uid);
        IMaidsoulKitchenTask.putTask(this.uid, () -> {
            return bindMod.versionLoaded && bindConfig.get() && IMaidsoulKitchenTask.TaskMixinMap.isApplyMixin(this.uid);
        }, bindTask);
        if (mixinClz.length > 0) {
            IMaidsoulKitchenTask.TaskMixinMap.put(this.uid, mixinClz);
        }
    }

    public static void init() {
    }
}
