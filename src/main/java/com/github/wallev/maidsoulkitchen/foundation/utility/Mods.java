package com.github.wallev.maidsoulkitchen.foundation.utility;

import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.Optional;

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
    YHCD("youkaishomecoming", "[2.2.3,)"),
    BNCD("brewinandchewin", "[3.0.0,)"),
    BNCD_LEGACY("brewinandchewin_legacy", "[,3.0.0)"),
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
    };

    public final String modId;
    public final boolean isLoaded;

    Mods(String modId) {
        this.modId = modId;
        this.isLoaded = this.isInstalled();
    }

    Mods(String modId, String versionRange) {
        this.modId = modId;
        this.isLoaded = this.isInstalled(versionRange);
    }

    protected boolean isInstalled() {
        return ModList.get().isLoaded(modId);
    }

    public ResourceLocation create(String path) {
        return VResourceLocation.create(modId, path);
    }

    // [x.x.x, )
    protected boolean isInstalled(String spec) {
        try {
            VersionRange versionRange = VersionRange.createFromVersionSpec(spec);
            Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(this.modId);
            if (modContainer.isPresent()) {
                if (versionRange.containsVersion(modContainer.get().getModInfo().getVersion())) {
                    return true;
                } else {
                    // 开发环境下，version 是空的，所以需要额外判断
                    return !FMLEnvironment.production;
                }
            }
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean allLoaded(String... modIds) {
        ModList modList = ModList.get();
        for (String modId : modIds)
            if (!modList.isLoaded(modId))
                return false;
        return true;
    }

    public static boolean hasLoaded(String... modIds) {
        ModList modList = ModList.get();
        for (String modId : modIds)
            if (modList.isLoaded(modId))
                return true;
        return false;
    }

    public static boolean hasLoaded(Mods... mods) {
        ModList modList = ModList.get();
        for (Mods mod : mods)
            if (mod.isInstalled())
                return true;
        return false;
    }

    public static void load() {
    }
}
