package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.util.ModUtil;
import org.jetbrains.annotations.NotNull;

public enum Mods implements IMods {
    TLM("touhou_little_maid"),
    MSK("maidsoulkitchen"),

    PATCHOULI("patchouli"),
    CLOTH_CONFIG("cloth_config"),

    JADE("jade"),
    TOP("theoneprobe"),

    SOPHISTICATED_STORAGE("sophisticatedstorage"),

    MAID_STORAGE_MANAGER("maid_storage_manager", "[1.13.7,)"),

    /*
        Farmer's Delight && Addons
     */
    FD("farmersdelight"),
    MD("miners_delight"),
    MND("mynethersdelight"),
    CD("cuisinedelight"),
    BD("barbequesdelight"),
    YHCD_LEGACY("youkaishomecoming", "[2.2.3,2.3.13)"),
    YHCD_NEW("youkaishomecoming", "[2.3.13,)"),
    YHCD("youkaishomecoming", "[2.2.3,)"),
    BNCD_LEGACY("brewinandchewin_legacy", "[,3.0.0)"),
    BNCD("brewinandchewin", "[3.0.0,)"),
    FRD("farmersrespite"),
    L2_HARVESTER("l2harvester"),
    COPPER_POT("copperpot"),
    DUNGEONS_DELIGHT("dungeonsdelight"),

    /*
        Let's DO
     */
    DAPI("doapi"),
    DHB("herbalbrews"),
    DV("vinery", "[1.4.29,]"),
    DBN("bloomingnature"),
    DBR("brewery"),
    DBP("beachparty"),
    DCL("candlelight", "[2.0.0,]"),
    DBK("bakery", "[2.0.0,]"),
    DFC("farm_and_charm", "[1.0.0,]"),
    DM("meadow"),

    SF("simplefarming"),
    FS("fruitstack"),

    /*
        Other
     */
    MS("supplementaries"),
    CP("crockpot"),
    DB("drinkbeer"),
    KK_LEGACY("kitchenkarrot", "[,0.5.4]"),
    KK_NEW("kitchenkarrot", "[0.6.2,]"),
    KK("kitchenkarrot"),

    TWT("thirst"),

    SS("sereneseasons"),
    ES("eclipticseasons"),

    MC("minecraft") {
        @Override
        public boolean isInstalled() {
            return true;
        }
    },
    KC("kaleidoscope_cookery"),
    TA("tea_aroma"),
    VTD("vintagedelight"), BAKERIES("bakeries"), IMD("immortalers_delight");

    private final String modId;
    private final String modName;
    private final String versionRange;
    private final boolean isLoaded;
    private final boolean versionLoaded;

    Mods(String modId) {
        this.modId = modId;
        this.modName = ModUtil.getModName(modId);
        this.isLoaded = this.isInstalled();
        this.versionLoaded = this.isInstalled();
        this.versionRange = "";
    }

    Mods(String modId, String versionRange) {
        this.modId = modId;
        this.modName = ModUtil.getModName(modId);
        this.versionRange = versionRange;
        this.isLoaded = this.isInstalled();
        this.versionLoaded = this.isInstalled(versionRange);
    }

    public static void init() {
    }

    public static Mods by(String mod) {
        return Mods.valueOf(mod);
    }

    public boolean isInstalled() {
        return ModUtil.isInstalled(modId);
    }

    protected boolean isInstalled(String versionRange) {
        return ModUtil.isInstalled(modId, versionRange);
    }


    @NotNull
    @Override
    public String getSerializedName() {
        return this.name();
    }

    @Override
    public String modId() {
        return modId;
    }

    @Override
    public String versionRange() {
        return versionRange;
    }

    @Override
    public boolean versionLoad() {
        return versionLoaded;
    }

    @Override
    public boolean load() {
        return isLoaded;
    }

    public String getModId() {
        return modId;
    }

    public String getModName() {
        return modName;
    }
}
