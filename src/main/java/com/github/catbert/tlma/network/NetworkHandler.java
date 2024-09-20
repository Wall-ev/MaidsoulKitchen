package com.github.catbert.tlma.network;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.network.message.*;
import com.github.catbert.tlma.network.message.client.ClientCookTaskRecActionMessage;
import com.github.catbert.tlma.network.message.client.ClientFarmTaskRuleActionMessage;
import com.github.catbert.tlma.network.message.client.ClientMaidTaskMessage;
import com.github.catbert.tlma.network.message.client.ClientSetCookTaskModeMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public final class NetworkHandler {
    private static final String VERSION = "1.0.0";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(TLMAddon.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        int i = 0;
        CHANNEL.registerMessage(i++, ToggleSideTabMessage.class, ToggleSideTabMessage::encode, ToggleSideTabMessage::decode, ToggleSideTabMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookTaskModeMessage.class, SetCookTaskModeMessage::encode, SetCookTaskModeMessage::decode, SetCookTaskModeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, RefreshMaidBrainMessage.class, RefreshMaidBrainMessage::encode, RefreshMaidBrainMessage::decode, RefreshMaidBrainMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientSetCookTaskModeMessage.class, ClientSetCookTaskModeMessage::encode, ClientSetCookTaskModeMessage::decode, ClientSetCookTaskModeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(i++, MaidTaskMessage.class, MaidTaskMessage::encode, MaidTaskMessage::decode, MaidTaskMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientMaidTaskMessage.class, ClientMaidTaskMessage::encode, ClientMaidTaskMessage::decode, ClientMaidTaskMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(i++, CookTaskRecActionMessage.class, CookTaskRecActionMessage::encode, CookTaskRecActionMessage::decode, CookTaskRecActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientCookTaskRecActionMessage.class, ClientCookTaskRecActionMessage::encode, ClientCookTaskRecActionMessage::decode, ClientCookTaskRecActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(i++, FarmTaskRuleActionMessage.class, FarmTaskRuleActionMessage::encode, FarmTaskRuleActionMessage::decode, FarmTaskRuleActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientFarmTaskRuleActionMessage.class, ClientFarmTaskRuleActionMessage::encode, ClientFarmTaskRuleActionMessage::decode, ClientFarmTaskRuleActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendToClientPlayer(Object message, Player player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }
}
