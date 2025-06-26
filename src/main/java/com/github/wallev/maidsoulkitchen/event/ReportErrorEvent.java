package com.github.wallev.maidsoulkitchen.event;

import com.github.wallev.maidsoulkitchen.util.classana.TaskLoadError;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ReportErrorEvent {

    @SubscribeEvent
    public static void reportError(PlayerEvent.PlayerLoggedInEvent event) {
        TaskLoadError.reportError(event.getEntity());
    }

}
