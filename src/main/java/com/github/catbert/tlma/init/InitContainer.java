package com.github.catbert.tlma.init;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.inventory.container.CompatFarmConfigureContainer;
import com.github.catbert.tlma.inventory.container.CookConfigureContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TLMAddon.MOD_ID);

    public static final RegistryObject<MenuType<CookConfigureContainer>> COOK_CONTAINER = CONTAINER_TYPE.register("cook_configer_container", () -> CookConfigureContainer.TYPE);
    public static final RegistryObject<MenuType<CompatFarmConfigureContainer>> COMPAT_FARM_CONTAINER = CONTAINER_TYPE.register("compat_farm_config_container", () -> CompatFarmConfigureContainer.TYPE);
}
