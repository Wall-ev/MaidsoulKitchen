package com.github.wallev.maidsoulkitchen;

import com.github.wallev.maidsoulkitchen.config.GeneralConfig;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.MaidBehaviorManager;
import com.github.wallev.maidsoulkitchen.init.*;
import com.github.wallev.maidsoulkitchen.lib.auto.event.AutoInit;
import com.github.wallev.maidsoulkitchen.lib.auto.event.EventAutoRegisterHelper;
import com.github.wallev.maidsoulkitchen.util.debug.AspectDebug;
import com.github.wallev.maidsoulkitchen.vhelper.IModInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.IOException;

@Mod(MaidsoulKitchen.MOD_ID)
public final class MaidsoulKitchen implements IModInfo {

    public MaidsoulKitchen() throws IOException {
        initRegister();
        initDebug();
        EventAutoRegisterHelper.autoSubscribeEventBus();
        AutoInit.Helper.init();
        new MaidBehaviorManager();
        GeneralConfig.init();
    }

    private static void initDebug() {
        AspectDebug.init();
    }

    private static void initRegister() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModContainers.CONTAINER_TYPE.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModEntities.MEMORY_MODULE_TYPES.register(modEventBus);
    }
}
