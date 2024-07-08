package com.github.catbert.tlma;

import com.github.catbert.tlma.config.GeneralConfig;
import com.github.catbert.tlma.init.InitItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TLMAddon.MOD_ID)
public final class TLMAddon {
    public static final String MOD_ID = "touhou_little_maid_addon";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public TLMAddon() {
        initRegister();
        initOtherRegister();
    }

    private static void initRegister() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        InitItems.ITEMS.register(modEventBus);
    }

    private static void initOtherRegister() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
    }
}
