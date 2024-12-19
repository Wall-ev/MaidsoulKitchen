package com.github.wallev.maidsoulkitchen.foundation.utility;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

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
    YHCD("youkaishomecoming"),
    BNCD("brewinandchewin"),

    /*
        Let's DO
     */
    DAPI("doapi"),
    DHB("herbalbrews"),
    DV("vinery"),
    DBP("beachparty"),
    DCL("candlelight"),
    DBK("bakery"),
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

    MC("minecraft") {
        @Override
        public boolean isLoaded() {
            return true;
        }
    };

    public final String modId;
    public final boolean isLoaded;
    Mods(String modId) {
        this.modId = modId;
        this.isLoaded = this.isLoaded();
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(modId);
    }

    public ResourceLocation create(String path) {
        return new ResourceLocation(modId, path);
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
            if (mod.isLoaded())
                return true;
        return false;
    }

}
