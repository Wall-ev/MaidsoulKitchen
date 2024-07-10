package com.github.catbert.tlma.init;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.effect.BurnProtectEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TLMAddon.MOD_ID);

    public static final RegistryObject<MobEffect> BURN_PROTECT = EFFECTS.register("burn_protect", BurnProtectEffect::new);

}
