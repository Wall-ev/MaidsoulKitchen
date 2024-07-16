package com.github.catbert.tlma.network;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.network.message.MaidTaskRecMessage;
import com.github.catbert.tlma.network.message.ToggleSideTabMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public final class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(TLMAddon.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        int i = 0;
        CHANNEL.registerMessage(i++, ToggleSideTabMessage.class, ToggleSideTabMessage::encode, ToggleSideTabMessage::decode, ToggleSideTabMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, MaidTaskRecMessage.class, MaidTaskRecMessage::encode, MaidTaskRecMessage::decode, MaidTaskRecMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

}
