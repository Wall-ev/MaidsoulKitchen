package com.catbert.tlma.foundation.utility;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

public enum Mods {

    /*
        Farmer's Delight && Addons
     */
    FD("farmersdelight"),
    MD("miners_delight"),
    CD("cuisinedelight"),
    YHC("youkaishomecoming"),

    /*
        Let's DO
     */
    DAPI("doapi"),
    DHB("herbalbrews"),
    DV("vinery"),

    SF("simplefarming"),
    FS("fruitstack"),
    CAUPONA("caupona"),
    CONVIVIUM("convivium"),

    /*
        Other
     */
    MS("supplementaries"),
    CP("crockpot"),
    DB("drinkbeer"),
    KK("kitchenkarrot"),

    SS("sereneseasons");


    public final String modId;
    public final boolean isLoaded;
    Mods(String modId) {
        this.modId = modId;
        this.isLoaded = isLoaded();
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
            if (mod.isLoaded)
                return true;
        return false;
    }

}
