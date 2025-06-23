package com.github.wallev.maidsoulkitchen.util.modutility;

import com.github.wallev.maidsoulkitchen.util.ModUtil;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;

public enum Mods {
    PATCHOULI("patchouli"),
    CLOTH_CONFIG("cloth_config"),

    JADE("jade"),
    TOP("theoneprobe"),

    SOPHISTICATED_STORAGE("sophisticatedstorage"),

    /*
        Farmer's Delight && Addons
     */
    FD("farmersdelight"),
    MD("miners_delight"),
    MND("mynethersdelight"),
    CD("cuisinedelight"),
    BD("barbequesdelight"),
    YHCD_LEGACY("youkaishomecoming", "[2.2.3,2.3.13)"),
    YHCD("youkaishomecoming", "[2.3.13,)"),
    BNCD_LEGACY("brewinandchewin_legacy", "[,3.0.0)"),
    BNCD("brewinandchewin", "[3.0.0,)"),
    FRD("farmersrespite"),

    /*
        Let's DO
     */
    DAPI("doapi"),
    DHB("herbalbrews"),
    DV("vinery", "[,1.4.28]"),
    DBP("beachparty"),
    DCL("candlelight", "[2.0.0,]"),
    DBK("bakery", "[2.0.0,]"),
    DFC("farm_and_charm"),

    SF("simplefarming"),
    FS("fruitstack"),

    /*
        Other
     */
    MS("supplementaries"),
    CP("crockpot"),
    DB("drinkbeer"),
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
    KC("kaleidoscope_cookery");

    public final String modId;
    public final boolean isLoaded;
    public final boolean versionLoaded;

    Mods(String modId) {
        this.modId = modId;
        this.isLoaded = this.isInstalled();
        this.versionLoaded = this.isInstalled();
    }

    Mods(String modId, String versionRange) {
        this.modId = modId;
        this.isLoaded = this.isInstalled();
        this.versionLoaded = this.isInstalled(versionRange);
    }

    public static void init() {
    }

    public boolean isInstalled() {
        return ModUtil.isInstalled(modId);
    }

    public ResourceLocation create(String path) {
        return VResourceLocation.create(modId, path);
    }

    protected boolean isInstalled(String versionRange) {
        return ModUtil.isInstalled(modId, versionRange);
    }
}
