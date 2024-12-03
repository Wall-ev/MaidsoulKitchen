package com.github.wallev.farmsoulkitchen.init;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.inventory.container.item.CookBagConfigContainer;
import com.github.wallev.farmsoulkitchen.inventory.container.item.CookBagContainer;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.BerryFarmConfigContainer;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.CompatMelonConfigContainer;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.FruitFarmConfigContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, FarmsoulKitchen.MOD_ID);

    public static final RegistryObject<MenuType<CookConfigContainer>> COOK_CONTAINER = CONTAINER_TYPE.register("cook_config_container", () -> CookConfigContainer.TYPE);
    public static final RegistryObject<MenuType<FruitFarmConfigContainer>> FRUIT_FARM_CONTAINER = CONTAINER_TYPE.register("fruit_farm_config_container", () -> FruitFarmConfigContainer.TYPE);
    public static final RegistryObject<MenuType<BerryFarmConfigContainer>> BERRY_FARM_CONTAINER = CONTAINER_TYPE.register("berry_farm_config_container", () -> BerryFarmConfigContainer.TYPE);
    public static final RegistryObject<MenuType<CompatMelonConfigContainer>> COMPAT_MELON_CONFIG_CONTAINER = CONTAINER_TYPE.register("compat_melon_config_container", () -> CompatMelonConfigContainer.TYPE);
    public static final RegistryObject<MenuType<CookBagContainer>> COOK_BAG_CONTAINER = CONTAINER_TYPE.register("culinary_hub_container", () -> CookBagContainer.TYPE);
    public static final RegistryObject<MenuType<CookBagConfigContainer>> COOK_BAG_CONFIG_CONTAINER = CONTAINER_TYPE.register("culinary_hub_config_container", () -> CookBagConfigContainer.TYPE);
}
