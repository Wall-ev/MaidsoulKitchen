package com.github.catbert.tlma.init;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.inventory.container.CookSettingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TLMAddon.MOD_ID);

    public static final RegistryObject<MenuType<CookSettingContainer>> COOKING_SETTING_CONTAINER = CONTAINER_TYPE.register("maid_config_container", () -> CookSettingContainer.TYPE);
}
