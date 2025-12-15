package com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ReportErrorEvent {
    private final BaseClazzCheckManager<?, ?> checkManager;

    private ReportErrorEvent(BaseClazzCheckManager<?, ?> checkManager) {
        this.checkManager = checkManager;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void reportError(PlayerEvent.PlayerLoggedInEvent event) {
        TaskLoadError.reportError((component -> {
            event.getEntity().sendSystemMessage(component);
        }), checkManager);
    }

    public static void init(BaseClazzCheckManager<?, ?> checkManager) {
        new ReportErrorEvent(checkManager);
    }

}
