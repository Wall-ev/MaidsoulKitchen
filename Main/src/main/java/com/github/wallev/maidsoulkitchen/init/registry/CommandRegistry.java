package com.github.wallev.maidsoulkitchen.init.registry;

import com.github.wallev.maidsoulkitchen.command.RootCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class CommandRegistry {
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        RootCommand.register(event.getDispatcher());
    }
}
