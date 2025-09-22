package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.util.EnumCodecUtil;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("Convert2MethodRef")
public enum TaskInfo implements ITaskInfo<Mods> {
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
            "furnace",
            "smelting",
            Mods.MC,
            true,
            () -> RegisterConfig.FURNACE_TASK_ENABLED),

    KC_POT("kc_pot",
            "pot",
            "pot",
            Mods.KC,
            true,
            () -> DevUtil.isDev()),
    KC_CHOPPING_BOARD("kc_chopping_board",
            "chopping_board",
            "chopping_board",
            Mods.KC,
            true,
            () -> DevUtil.isDev()),

    FD_COOK_POT("fd_cooking_pot",
            "cooking_pot",
            "cooking",
            Mods.FD,
            true,
            () -> RegisterConfig.FD_COOK_POT_TASK_ENABLED),
    FD_CUTTING_BOARD("fd_cutting_board",
            "cutting_board",
            "cutting",
            Mods.FD,
            true,
            () -> DevUtil.isDev()),

    CD_CUISINE_SKILLET("cd_cuisine_skillet",
            "cuisine_skillet",
            "cuisine",
            Mods.CD,
            true,
            () -> DevUtil.isDev()),

    MD_COOK_POT("md_copper_pot",
            "copper_pot",
            "cooking",
            Mods.MD,
            true,
            () -> RegisterConfig.MD_COOK_POT_TASK_ENABLED),

    COPPER_POT("copper_pot",
            "copper_pot",
            "cooking",
            Mods.COPPER_POT,
            true,
            () -> DevUtil.isDev()),

    MONSTER_POT("monster_pot",
            "monster_pot",
            "monster_cooking",
            Mods.DUNGEONS_DELIGHT,
            true,
            () -> DevUtil.isDev()),

    BNC_KEY("bnc_key",
            "keg",
            "fermenting",
            Mods.BNCD,
            true,
            () -> RegisterConfig.BNC_KEY_TASK_ENABLED),

    FR_KETTLE("fr_kettle",
            "kettle",
            "brewing",
            Mods.FRD,
            true,
            () -> RegisterConfig.FR_KETTLE_TASK_ENABLED),

    BD_BASIN("bd_basin",
            "basin",
            "skewering",
            Mods.BD,
            true,
            () -> DevUtil.isDev()),
    BD_GRILL("bd_grill",
            "grill",
            "grilling",
            Mods.BD,
            true,
            () -> DevUtil.isDev()),

    YHC_MOKA("yhc_moka_pot",
            "moka_pot",
            "moka_pot",
            Mods.YHCD,
            true,
            () -> RegisterConfig.YHC_MOKA_TASK_ENABLED),
    YHC_TEA_KETTLE("yhc_tea_kettle",
            "kettle",
            "kettle",
            Mods.YHCD,
            true,
            () ->RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED),
    YHC_DRYING_RACK("yhc_drying_rack",
            "drying_rack",
            "drying_rack",
            Mods.YHCD,
            true,
            () -> RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED),
    YHC_FERMENTATION_TANK("yhc_fermentation_tank",
            "fermentation_tank",
            "fermentation",
            Mods.YHCD_NEW,
            true,
            () -> RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED),

    KK_BREW_BARREL("kk_brew_barrel",
            "brewing_barrel",
            "brewing_barrel",
            Mods.KK_NEW,
            true,
            () -> RegisterConfig.KK_BREW_BARREL),
    KK_AIR_COMPRESSOR("kk_air_compressor",
            "air_compressor",
            "air_compressing",
            Mods.KK,
            true,
            () -> RegisterConfig.KK_AIR_COMPRESSOR),

    DB_BEER("drinkbeer_beerbarrel",
            "beer_barrel",
            "brewing",
            Mods.DB,
            true,
            () -> RegisterConfig.DB_BEER_TASK_ENABLED),

    CP_CROCK_POT("cp_crock_pot",
            "crock_pot",
            "crock_pot_cooking",
            Mods.CP,
            true,
            () -> RegisterConfig.CP_CROk_POT_TASK_ENABLED),

    DFC_STOVE("stove",
            Mods.DFC,
            true,
            () -> DevUtil.isDev()),
    DFC_ROASTER("roaster",
            Mods.DFC,
            true,
            () -> DevUtil.isDev()),
    DFC_COOKING_POT("cooking_pot",
            Mods.DFC,
            true,
            () -> DevUtil.isDev()),

    DBK_COOKING_POT("cooking_pot",
            Mods.DBK,
            true,
            () -> DevUtil.isDev()),

    DCL_COOKING_POT("cooking_pot",
            Mods.DCL,
            true,
            () -> DevUtil.isDev()),
    DCL_COOKING_PAN("cooking_pan",
            Mods.DCL,
            true,
            () -> DevUtil.isDev()),

    DBP_MINI_FRIDGE("mine_fridge",
            Mods.DBP,
            true,
            () -> DevUtil.isDev()),
    DBP_PALM_BAR("palm_bar",
            Mods.DBP,
            true,
            () -> DevUtil.isDev()),

    DM_CHEESE_FORM("cheese_form",
            Mods.DM,
            true,
            () -> DevUtil.isDev()),


    /**
     * 不是实质的任务，都是集成在浆果任务里，只是给{@link TaskClassAnalyzer}分析和拦截使用
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
     * 不是实质的任务，都是集成在果树任务里，只是给{@link TaskClassAnalyzer}分析和拦截使用
     */
    FRUIT_SIMPLE_FARMING("fruit_simple_farming",
            Mods.SF,
            () -> RegisterConfig.FRUIT_FARM_TASK_ENABLED),
    FRUIT_COMPAT("fruit_compat",
            Mods.MC,
            () -> RegisterConfig.FRUIT_FARM_TASK_ENABLED),


    /**
     * 不是实质的任务，都是旗帜渲染，只是给{@link TaskClassAnalyzer}分析和拦截使用
     */
    LAYER_BAKERY("layer_bakery",
            Mods.DBK,
            () -> DevUtil.isDev()),
    LAYER_BEACHPARTY("layer_beachparty",
            Mods.DBP,
            () -> DevUtil.isDev()),
    LAYER_BLOOMINGNATURE("layer_bloomingnature",
            Mods.DBN,
            () -> DevUtil.isDev()),
    LAYER_BREWERY("layer_brewery",
            Mods.DBR,
            () -> DevUtil.isDev()),
    LAYER_CANDLELIGHT("layer_candlelight",
            Mods.DCL,
            () -> DevUtil.isDev()),
    LAYER_HERBALBREWS("layer_herbalbrews",
            Mods.DHB,
            () -> DevUtil.isDev()),
    LAYER_MEADOW("layer_meadow",
            Mods.DM,
            () -> DevUtil.isDev()),
    LAYER_VINERY("layer_vinery",
            Mods.DBR,
            () -> DevUtil.isDev()),

    ;

    public static final TaskInfo[] VALUES = values();
    public static final Codec<TaskInfo> CODEC = EnumCodecUtil.fromEnum(() -> VALUES);

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
        this.oldName = VResourceLocation.createMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, Config bindConfig) {
        this.oldName = VResourceLocation.createMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this.oldName = VResourceLocation.createMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, Config bindConfig) {
        this.oldName = VResourceLocation.createMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this("", uid, bindMod, concatModId, bindConfig);
    }

    TaskInfo(String uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig) {
        this(uid, bindMod, false, bindConfig);
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Config bindConfig) {
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId) {
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> true;
    }

    TaskInfo(String uid, Mods bindMod, Config bindConfig) {
        this(uid, bindMod, false, bindConfig);
    }

    TaskInfo(String uid, Mods bindMod) {
        this(uid, bindMod, false);
    }

    public static void init() {
    }

    private static ResourceLocation convertUid(String oldName, String uid, Mods bindMod, boolean concatModId) {
//        if (oldName.isEmpty()) {
            return concatModId ? VResourceLocation.createMod(bindMod.modId() + "_" + uid) : VResourceLocation.createMod(uid);
//        }
//        return VResourceLocation.createMod(oldName);
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

    @Override
    public String getUidStr() {
        return uid.toString();
    }

    @Override
    public Mods getBindMod() {
        return bindMod;
    }

    public boolean modVersionLoaded() {
        return bindMod.versionLoad();
    }

    public boolean configEnabled() {
        return bindConfig.get();
    }

    public final boolean canLoad() {
        return canLoadWithoutCheckClazz() && TaskModClazzManager.clazzLoad(this.uid.toString());
    }

    @Override
    public boolean canLoadWithoutCheckClazz() {
        return modVersionLoaded() && configEnabled();
    }

    /**
     * use {@link #getUid()} instead
     */
    @Deprecated
    public String getOldName() {
        return oldName;
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return getUid().toString();
    }

    private interface Config {
        boolean canLoad();
    }
}
