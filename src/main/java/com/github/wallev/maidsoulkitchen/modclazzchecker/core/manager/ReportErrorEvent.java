package com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@OnlyIn(Dist.CLIENT)
public class ReportErrorEvent {
    private final BaseClazzCheckManager<?, ?> checkManager;

    private ReportErrorEvent(BaseClazzCheckManager<?, ?> checkManager) {
        this.checkManager = checkManager;
        NeoForge.EVENT_BUS.register(this);
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
