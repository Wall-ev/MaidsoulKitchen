package com.github.wallev.farmsoulkitchen.init;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.effect.BurnProtectEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FarmsoulKitchen.MOD_ID);

    public static final RegistryObject<MobEffect> BURN_PROTECT = EFFECTS.register("burn_protect", BurnProtectEffect::new);

}
