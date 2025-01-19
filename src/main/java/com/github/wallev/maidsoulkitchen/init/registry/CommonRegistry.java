package com.github.wallev.maidsoulkitchen.init.registry;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookContainerSerializerRulesManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import com.github.wallev.maidsoulkitchen.util.AnnotatedInstanceUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CommonRegistry {
    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::init);
        event.enqueueWork(CommonRegistry::modApiInit);
    }

    private static void modApiInit() {
        MaidsoulKitchen.EXTENSIONS = AnnotatedInstanceUtil.getModExtensions();
        CookRecRecipeInitializerManager.register();
        CookRecIngredientSerializerManager.register();
        CookContainerSerializerRulesManager.register();
    }
}