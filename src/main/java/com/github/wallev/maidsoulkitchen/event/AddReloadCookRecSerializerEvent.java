package com.github.wallev.maidsoulkitchen.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID)
public class AddReloadCookRecSerializerEvent {

    @SubscribeEvent
    public static void addReloadCookRecSerializer(AddReloadListenerEvent event) {

//        CookRecipeSerializerManager.initialSerializerData();
    }

    @SubscribeEvent
    public static void loadWorldIn(ServerStartedEvent event) {
        CookRecRecipeInitializerManager.initializerData(event.getServer().getLevel(Level.OVERWORLD));
        int a = 1;
//        CookRecipeSerializerManager.initialSerializerData();
    }
}
