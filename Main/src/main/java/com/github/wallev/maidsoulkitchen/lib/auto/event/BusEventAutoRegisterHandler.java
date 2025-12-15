package com.github.wallev.maidsoulkitchen.lib.auto.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BusEventAutoRegisterHandler {

    @SubscribeEvent
    public static void onRun(final InterModEnqueueEvent event) {
        EventAutoRegisterHelper.autoSubscribeInterModEnqueueEvent(event);
    }

    @SubscribeEvent
    public static void onRun(final FMLCommonSetupEvent event) {
        EventAutoRegisterHelper.autoSubscribeFMLCommonSetupEvent(event);
    }

}
