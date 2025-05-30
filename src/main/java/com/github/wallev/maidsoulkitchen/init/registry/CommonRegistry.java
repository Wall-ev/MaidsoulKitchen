package com.github.wallev.maidsoulkitchen.init.registry;

import com.github.wallev.maidsoulkitchen.debug.EnableMaidDebug;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CommonRegistry {
    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::init);
        event.enqueueWork(() -> {
            if (!FMLEnvironment.production) {
                MinecraftForge.EVENT_BUS.register(new EnableMaidDebug());
            }
        });
    }
}