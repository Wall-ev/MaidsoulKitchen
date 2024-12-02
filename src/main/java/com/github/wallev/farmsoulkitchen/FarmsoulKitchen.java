package com.github.wallev.farmsoulkitchen;

import com.github.wallev.farmsoulkitchen.config.GeneralConfig;
import com.github.wallev.farmsoulkitchen.init.InitContainer;
import com.github.wallev.farmsoulkitchen.init.InitEffects;
import com.github.wallev.farmsoulkitchen.init.InitItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FarmsoulKitchen.MOD_ID)
public final class FarmsoulKitchen {
    public static final String MOD_ID = "farmsoulkitchen";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public FarmsoulKitchen() {
        initRegister();
        initConfigureRegister();
    }

    private static void initRegister() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        InitItems.ITEMS.register(modEventBus);
        InitEffects.EFFECTS.register(modEventBus);
        InitContainer.CONTAINER_TYPE.register(modEventBus);
    }

    private static void initConfigureRegister() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
    }
}
