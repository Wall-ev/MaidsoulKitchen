package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class VMessageHelper {

    public static void sendSystemMessage(Player player, Component component) {
        player.sendMessage(component, Util.NIL_UUID);
    }

}
