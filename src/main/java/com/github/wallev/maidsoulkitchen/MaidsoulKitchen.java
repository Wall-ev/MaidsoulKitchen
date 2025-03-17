package com.github.wallev.maidsoulkitchen;

import com.github.wallev.maidsoulkitchen.config.GeneralConfig;
import com.github.wallev.maidsoulkitchen.init.MkContainer;
import com.github.wallev.maidsoulkitchen.init.MkEffects;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.verhelper.IModInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MaidsoulKitchen.MOD_ID)
public final class MaidsoulKitchen implements IModInfo {

    public MaidsoulKitchen() {
        initRegister();
        initConfigureRegister();
    }

    private static void initRegister() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MkItems.ITEMS.register(modEventBus);
        MkEffects.EFFECTS.register(modEventBus);
        MkContainer.CONTAINER_TYPE.register(modEventBus);
        MkEntities.MEMORY_MODULE_TYPES.register(modEventBus);
    }

    private static void initConfigureRegister() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
    }
}
