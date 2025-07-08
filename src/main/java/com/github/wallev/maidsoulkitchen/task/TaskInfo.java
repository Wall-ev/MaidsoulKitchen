package com.github.wallev.maidsoulkitchen.task;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum TaskInfo {
    /**
     * 没人有何实质作用，只是给{@link TaskClassAnalyzer}做默认值使用（骗过编译器x）
     */
    NONE("",
            Mods.MC),

    /**
     * 默认烹饪任务，什么都不做，只给烹饪找不到任务时使用
     */
    IDLE("idle",
            Mods.MC),
    COOK("cook",
            Mods.MC,
            () -> RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED),

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
            () -> RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED),

    ECLIPTICSSEASONS_FARM("eclipticseasons_farm",
            Mods.ES,
            () -> RegisterConfig.ECLIPTICSEASONS_FARM_TASK_ENABLED),

    FURNACE("furnace",
            "funrance",
            Mods.MC,
            true,
            () -> RegisterConfig.FURNACE_TASK_ENABLED),

    KC_POT("kc_pot",
            "pot",
            Mods.KC,
            true,
            () -> RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED),
    KC_CHOPPING_BOARD("kc_chopping_board",
            "chopping_board",
            Mods.KC,
            true,
            () -> RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED),

    FD_COOK_POT("fd_cooking_pot",
            "cooking_pot",
            Mods.FD,
            true,
            () -> RegisterConfig.FD_COOK_POT_TASK_ENABLED),
    FD_CUTTING_BOARD("fd_cutting_board",
            "cutting_board",
            Mods.FD,
            true,
            () -> RegisterConfig.FD_CUTTING_BOARD_TASK_ENABLED),

    CD_CUISINE_SKILLET("cd_cuisine_skillet",
            "cuisine_skillet",
            Mods.CD,
            true,
            () -> RegisterConfig.CD_CUISINE_SKILLET_TASK_ENABLED),

    MD_COOK_POT("md_copper_pot",
            "copper_pot",
            Mods.MD,
            true,
            () -> RegisterConfig.MD_COOK_POT_TASK_ENABLED),

    COPPER_POT("copper_pot",
            Mods.COPPER_POT,
            true,
            () -> MaidsoulKitchen.DEBUG),

    MONSTER_POT("monster_pot",
            Mods.DUNGEONS_DELIGHT,
            true,
            () -> MaidsoulKitchen.DEBUG),

    BNC_KEY("bnc_key",
            "keg",
            Mods.BNCD,
            true,
            () -> RegisterConfig.BNC_KEY_TASK_ENABLED),

    FR_KETTLE("fr_kettle",
            "kettle",
            Mods.FRD,
            true,
            () -> RegisterConfig.FR_KETTLE_TASK_ENABLED),

    BD_BASIN("bd_basin",
            "basin",
            Mods.BD,
            true,
            () -> RegisterConfig.BD_BASIN_TASK_ENABLED),
    BD_GRILL("bd_grill",
            "grill",
            Mods.BD,
            true,
            () -> RegisterConfig.BD_GRILL_TASK_ENABLED),

    YHC_MOKA("yhc_moka_pot",
            "moka_pot",
            Mods.YHCD,
            true,
            () -> RegisterConfig.YHC_MOKA_TASK_ENABLED),
    YHC_TEA_KETTLE("yhc_tea_kettle",
            "tea_kettle",
            Mods.YHCD,
            true,
            () ->RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED),
    YHC_DRYING_RACK("yhc_drying_rack",
            "drying_rack",
            Mods.YHCD,
            true,
            () -> RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED),
    YHC_FERMENTATION_TANK("yhc_fermentation_tank",
            "fermentation_tank",
            Mods.YHCD,
            true,
            () -> RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED),

    DFC_STOVE("stove",
            Mods.DFC,
            true,
            () -> MaidsoulKitchen.DEBUG),
    DFC_ROASTER("roaster",
            Mods.DFC,
            true,
            () -> MaidsoulKitchen.DEBUG),
    DFC_COOKING_POT("cooking_pot",
            Mods.DFC,
            true,
            () -> MaidsoulKitchen.DEBUG),

    DBK_COOKING_POT("cooking_pot",
            Mods.DBK,
            true,
            () -> MaidsoulKitchen.DEBUG),

    DCL_COOKING_POT("cooking_pot",
            Mods.DCL,
            true,
            () -> MaidsoulKitchen.DEBUG),
    DCL_COOKING_PAN("cooking_pan",
            Mods.DCL,
            true,
            () -> MaidsoulKitchen.DEBUG),

    DBP_MINI_FRIDGE("mine_fridge",
            Mods.DBP,
            true,
            () -> MaidsoulKitchen.DEBUG),
    DBP_PALM_BAR("palm_bar",
            Mods.DBP,
            true,
            () -> MaidsoulKitchen.DEBUG),

    DM_CHEESE_FORM("cheese_form",
            Mods.DM,
            true,
            () -> MaidsoulKitchen.DEBUG),

    KK_BREW_BARREL("kk_brew_barrel",
            "brew_barrel",
            Mods.KK,
            true,
            () -> RegisterConfig.KK_BREW_BARREL),
    KK_AIR_COMPRESSOR("kk_air_compressor",
            "air_compressor",
            Mods.KK,
            true,
            () -> RegisterConfig.KK_AIR_COMPRESSOR),

    DB_BEER("drinkbeer_beerbarrel",
            "beerbarrel",
            Mods.DB,
            true,
            () -> RegisterConfig.DB_BEER_TASK_ENABLED),
    CP_CROCK_POT("cp_crock_pot",
            "crock_pot",
            Mods.CP,
            true,
            () -> RegisterConfig.CP_CROk_POT_TASK_ENABLED),


    /**
     * 不是实质的任务，都是集成在浆果任务里，只是给{@link TaskClassAnalyzer}分析使用
     */
    BERRY_MINECRAFT("berry_minecraft",
            Mods.MC,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),
    BERRY_L2_HARVESTER("berry_l2_harvester",
            Mods.L2_HARVESTER,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),
    BERRY_FARMERS_RESPITE_GREEN_TEA("berry_farmersrespite_greentea",
            Mods.FRD,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),
    BERRY_FARMERS_RESPITE_YELLOW_TEA("berry_farmersrespite_yellowtea",
            Mods.FRD,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),
    BERRY_FARMERS_RESPITE_BLACK_TEA("berry_farmersrespite_blacktea",
            Mods.FRD,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),
    BERRY_SIMPLE_FARMING("berry_simple_farming",
            Mods.SF,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),
    BERRY_COMPAT("berry_compat",
            Mods.MC,
            () -> RegisterConfig.BERRY_FARM_TASK_ENABLED),

    /**
     * 不是实质的任务，都是集成在果树任务里，只是给{@link TaskClassAnalyzer}分析使用
     */
    FRUIT_SIMPLE_FARMING("fruit_simple_farming",
            Mods.SF,
            () -> RegisterConfig.FRUIT_FARM_TASK_ENABLED),
    FRUIT_COMPAT("fruit_compat",
            Mods.MC,
            () -> RegisterConfig.FRUIT_FARM_TASK_ENABLED),
    ;

    private String oldName = "";
    private final ResourceLocation uid;
    private final Mods bindMod;
    private final Supplier<Boolean> bindConfig;

    TaskInfo(ResourceLocation uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this.oldName = oldName;
        this.uid = concatModId ? VResourceLocation.createMod(bindMod.modId + "_" + uid) : VResourceLocation.createMod(uid);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this("", uid, bindMod, concatModId, bindConfig);
    }

    TaskInfo(String uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this(uid, bindMod, false, bindConfig);
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Config bindConfig) {
        this.uid = concatModId ? VResourceLocation.createMod(bindMod.modId + "_" + uid) : VResourceLocation.createMod(uid);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId) {
        this.uid = concatModId ? VResourceLocation.createMod(bindMod.modId + "_" + uid) : VResourceLocation.createMod(uid);
        this.bindMod = bindMod;
        this.bindConfig = () -> true;
    }

    TaskInfo(String uid, Mods bindMod) {
        this(uid, bindMod, false);
    }

    public static void init() {
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

    public ResourceLocation getUid() {
        return uid;
    }

    public Mods getBindMod() {
        return bindMod;
    }

    public boolean modVersionLoaded() {
        return bindMod.versionLoaded;
    }

    public boolean configEnabled() {
        return bindConfig.get();
    }

    /**
     * use {@link #getUid()} instead
     */
    @Deprecated
    public String getOldName() {
        return oldName;
    }

    private interface Config {
        boolean canLoad();
    }
}
