package com.github.wallev.maidsoulkitchen.init;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CompatRegistry {
    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
    }

    private static void checkModLoad(Mods mod, Runnable runnable) {
        if (mod.load()) {
            runnable.run();
        }
    }
}
