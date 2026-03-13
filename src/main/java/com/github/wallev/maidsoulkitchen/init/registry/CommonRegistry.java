package com.github.wallev.maidsoulkitchen.init.registry;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;


import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.IOException;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class CommonRegistry {
    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(CommonRegistry::modApiInit);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerMaidStorageEventListener(FMLCommonSetupEvent event) {
        event.enqueueWork(CommonRegistry::mccInit);
    }

    public static void mccInit() {
        Mods.init();
        TaskInfo.init();
        try {
            TaskModClazzManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void modApiInit() {
    }
}