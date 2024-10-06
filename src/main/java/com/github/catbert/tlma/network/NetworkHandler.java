package com.github.catbert.tlma.network;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.network.message.*;
import com.github.catbert.tlma.network.message.client.ClientCookTaskRecActionMessage;
import com.github.catbert.tlma.network.message.client.ClientFarmTaskRuleActionMessage;
import com.github.catbert.tlma.network.message.client.ClientSetCookTaskModeMessage;
import com.github.catbert.tlma.network.message.client.ClientSetFruitFarmSearchYOffsetMessage;
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
        // Server
        CHANNEL.registerMessage(i++, ToggleCookBagGuiSideTabMessage.class, ToggleCookBagGuiSideTabMessage::encode, ToggleCookBagGuiSideTabMessage::decode, ToggleCookBagGuiSideTabMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookBagBindModeMessage.class, SetCookBagBindModeMessage::encode, SetCookBagBindModeMessage::decode, SetCookBagBindModeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        // Server && Client
        CHANNEL.registerMessage(i++, SetCookTaskModeMessage.class, SetCookTaskModeMessage::encode, SetCookTaskModeMessage::decode, SetCookTaskModeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientSetCookTaskModeMessage.class, ClientSetCookTaskModeMessage::encode, ClientSetCookTaskModeMessage::decode, ClientSetCookTaskModeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(i++, CookTaskRecActionMessage.class, CookTaskRecActionMessage::encode, CookTaskRecActionMessage::decode, CookTaskRecActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientCookTaskRecActionMessage.class, ClientCookTaskRecActionMessage::encode, ClientCookTaskRecActionMessage::decode, ClientCookTaskRecActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(i++, FarmTaskRuleActionMessage.class, FarmTaskRuleActionMessage::encode, FarmTaskRuleActionMessage::decode, FarmTaskRuleActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientFarmTaskRuleActionMessage.class, ClientFarmTaskRuleActionMessage::encode, ClientFarmTaskRuleActionMessage::decode, ClientFarmTaskRuleActionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(i++, SetFruitFarmSearchYOffsetMessage.class, SetFruitFarmSearchYOffsetMessage::encode, SetFruitFarmSearchYOffsetMessage::decode, SetFruitFarmSearchYOffsetMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClientSetFruitFarmSearchYOffsetMessage.class, ClientSetFruitFarmSearchYOffsetMessage::encode, ClientSetFruitFarmSearchYOffsetMessage::decode, ClientSetFruitFarmSearchYOffsetMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        // Client

    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendToClientPlayer(Object message, Player player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }
}
