package com.github.catbert.tlma.init;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.inventory.container.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TLMAddon.MOD_ID);

    public static final RegistryObject<MenuType<CookConfigContainer>> COOK_CONTAINER = CONTAINER_TYPE.register("cook_configer_container", () -> CookConfigContainer.TYPE);
    public static final RegistryObject<MenuType<CompatFarmConfigContainer>> COMPAT_FARM_CONTAINER = CONTAINER_TYPE.register("compat_farm_config_container", () -> CompatFarmConfigContainer.TYPE);
    public static final RegistryObject<MenuType<FruitFarmConfigContainer>> FRUIT_FARM_CONTAINER = CONTAINER_TYPE.register("fruit_farm_config_container", () -> FruitFarmConfigContainer.TYPE);
    public static final RegistryObject<MenuType<NoConfigContainer>> NO_CONFIG_CONTAINER = CONTAINER_TYPE.register("no_config_container", () -> NoConfigContainer.TYPE);
    public static final RegistryObject<MenuType<CompatMelonConfigContainer>> COMPAT_MELON_CONFIG_CONTAINER = CONTAINER_TYPE.register("compat_melon_config_container", () -> CompatMelonConfigContainer.TYPE);
}
