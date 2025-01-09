package com.github.wallev.maidsoulkitchen.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID)
public class AddReloadCookRecSerializerEvent {

    @SubscribeEvent
    public static void addReloadCookRecSerializer(AddReloadListenerEvent event) {
    }
}
