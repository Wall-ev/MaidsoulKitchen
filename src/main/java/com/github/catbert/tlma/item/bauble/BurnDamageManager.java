package com.github.catbert.tlma.item.bauble;

import com.github.catbert.tlma.foundation.utility.Mods;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import org.apache.commons.compress.utils.Lists;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;

import java.util.List;

import static com.github.catbert.tlma.foundation.utility.Mods.*;

public final class BurnDamageManager {
    private final List<ResourceKey<DamageType>> BURN_DAMAGE_TYPES = Lists.newArrayList();
    private static final BurnDamageManager INSTANCE = new BurnDamageManager();

    private BurnDamageManager() {
        addDamageType(MC, DamageTypes.IN_FIRE);
        addDamageType(MC, DamageTypes.HOT_FLOOR);
        addDamageType(FD, ModDamageTypes.STOVE_BURN);
    }

    private void addDamageType(Mods mod, ResourceKey<DamageType> damageType) {
        if (mod.isLoaded) {
            BURN_DAMAGE_TYPES.add(damageType);
        }
    }

    public static BurnDamageManager getInstance() {
        return INSTANCE;
    }

    public static List<ResourceKey<DamageType>> getBurnDamageTypes() {
        return INSTANCE.BURN_DAMAGE_TYPES;
    }

    public static void addBurnDamageType(ResourceKey<DamageType> damageType) {
        INSTANCE.BURN_DAMAGE_TYPES.add(damageType);
    }

    public static void clearBurnDamageTypes() {
        INSTANCE.BURN_DAMAGE_TYPES.clear();
    }
}
