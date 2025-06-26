package com.github.wallev.maidsoulkitchen.task;

import com.github.wallev.maidsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum TaskInfo {
    NONE("", Mods.MC, () -> null),

    COMPAT_MELON_FARM("compat_melon",
            Mods.MC,
            () -> RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED),
    BERRY_FARM("berries_farm",
            Mods.MC,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),
    FRUIT_FARM("fruit_farm",
            Mods.MC,
            () -> RegisterConfig.FRUIT_FARM_TASK_ENABLED),

    FEED_ANIMAL_T("feed_animal_t",
            Mods.MC,
            () -> RegisterConfig.FEED_ANIMAL_T_TASK_ENABLED),

    SERENESEASONS_FARM("sereneseasons_farm",
            Mods.SS,
            () -> RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED,
            ModGroup.SERENESEASONS),

    ECLIPTICSSEASONS_FARM("eclipticseasons_farm",
            Mods.ES,
            () -> RegisterConfig.ECLIPTICSEASONS_FARM_TASK_ENABLED,
            ModGroup.TEAM_TEA),

    FURNACE("furnace",
            Mods.MC,
            () -> RegisterConfig.FURNACE_TASK_ENABLED),

    KC_POT("kc_pot",
            Mods.KC,
            () -> RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED,
            ModGroup.YSBBBBBB),
    KC_CHOPPING_BOARD("kc_chopping_board",
            Mods.KC,
            () -> RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED,
            ModGroup.YSBBBBBB),

    FD_COOK_POT("fd_cooking_pot",
            Mods.FD,
            () -> RegisterConfig.FD_COOK_POT_TASK_ENABLED,
            ModGroup.VECTORWING),
    FD_CUTTING_BOARD("fd_cutting_board",
            Mods.FD,
            () -> RegisterConfig.FD_CUTTING_BOARD_TASK_ENABLED,
            ModGroup.VECTORWING),

    CD_CUISINE_SKILLET("cd_cuisine_skillet",
            Mods.CD,
            () -> RegisterConfig.CD_CUISINE_SKILLET_TASK_ENABLED,
            ModGroup.XKMC),

    MD_COOK_POT("md_copper_pot",
            Mods.MD,
            () -> RegisterConfig.MD_COOK_POT_TASK_ENABLED,
            ModGroup.SAMMY),

    BNC_KEY("bnc_key",
            Mods.BNCD,
            () -> RegisterConfig.BNC_KEY_TASK_ENABLED,
            ModGroup.UMPAZ),

    FR_KETTLE("fr_kettle",
            Mods.FRD,
            () -> RegisterConfig.FR_KETTLE_TASK_ENABLED,
            ModGroup.UMPAZ),

    BD_BASIN("bd_basin",
            Mods.BD,
            () -> RegisterConfig.BD_BASIN_TASK_ENABLED,
            ModGroup.MAO),
    BD_GRILL("bd_grill",
            Mods.BD,
            () -> RegisterConfig.BD_GRILL_TASK_ENABLED,
            ModGroup.MAO),

    YHC_MOKA("yhc_moka_pot",
            Mods.YHCD,
            () -> RegisterConfig.YHC_MOKA_TASK_ENABLED,
            ModGroup.XKMC),
    YHC_TEA_KETTLE("yhc_tea_kettle",
            Mods.YHCD,
            () ->RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED,
            ModGroup.XKMC),
    YHC_DRYING_RACK("yhc_drying_rack",
            Mods.YHCD,
            () -> RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED,
            ModGroup.XKMC),
    YHC_FERMENTATION_TANK("yhc_fermentation_tank",
            Mods.YHCD,
            () -> RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED,
            ModGroup.XKMC),

    KK_BREW_BARREL("kk_brew_barrel",
            Mods.KK,
            () -> RegisterConfig.KK_BREW_BARREL,
            ModGroup.TT_432),
    KK_AIR_COMPRESSOR("kk_air_compressor",
            Mods.KK,
            () -> RegisterConfig.KK_AIR_COMPRESSOR,
            ModGroup.TT_432),

    DB_BEER("drinkbeer_beerbarrel",
            Mods.DB,
            () -> RegisterConfig.DB_BEER_TASK_ENABLED,
            ModGroup.LEKAVAR_LMA),
    CP_CROCK_POT("cp_crock_pot",
            Mods.CP,
            () -> RegisterConfig.CP_CROk_POT_TASK_ENABLED,
            ModGroup.SIHENZHANG),
    ;
    public final ResourceLocation uid;
    public final Mods bindMod;
    public final Supplier<ForgeConfigSpec.BooleanValue> bindConfig;
    public final ModGroup group;

    TaskInfo(ResourceLocation uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, ModGroup modGroup) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.bindConfig = bindConfig;
        this.group = modGroup;
    }

    TaskInfo(String uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, ModGroup group) {
        this(VResourceLocation.createMod(uid), bindMod, bindConfig, group);
    }

    TaskInfo(String uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this(VResourceLocation.createMod(uid), bindMod, bindConfig, ModGroup.NONE);
    }

    public static void init(){
    }

    public static TaskInfo by(String key) {
        return TaskInfo.valueOf(key);
    }

    @Nullable
    public static TaskInfo by(ResourceLocation taskUid) {
        for (TaskInfo value : TaskInfo.values()) {
            if (value.uid.equals(taskUid)) {
                return value;
            }
        }
        return null;
    }
}
