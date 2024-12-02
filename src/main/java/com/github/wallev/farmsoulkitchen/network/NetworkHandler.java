package com.github.wallev.farmsoulkitchen.network;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.network.message.*;
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

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(FarmsoulKitchen.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        int i = 0;
        // Server
        CHANNEL.registerMessage(i++, ToggleCookBagGuiSideTabMessage.class, ToggleCookBagGuiSideTabMessage::encode, ToggleCookBagGuiSideTabMessage::decode, ToggleCookBagGuiSideTabMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookBagBindModeMessage.class, SetCookBagBindModeMessage::encode, SetCookBagBindModeMessage::decode, SetCookBagBindModeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookDataModeMessage.class, SetCookDataModeMessage::encode, SetCookDataModeMessage::decode, SetCookDataModeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionCookDataRecMessage.class, ActionCookDataRecMessage::encode, ActionCookDataRecMessage::decode, ActionCookDataRecMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetFruitFarmSearchYOffsetMessage.class, SetFruitFarmSearchYOffsetMessage::encode, SetFruitFarmSearchYOffsetMessage::decode, SetFruitFarmSearchYOffsetMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionBerryFarmRuleMessage.class, ActionBerryFarmRuleMessage::encode, ActionBerryFarmRuleMessage::decode, ActionBerryFarmRuleMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionFruitFarmRuleMessage.class, ActionFruitFarmRuleMessage::encode, ActionFruitFarmRuleMessage::decode, ActionFruitFarmRuleMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        // Server && Client

        // Client

    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendToClientPlayer(Object message, Player player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }
}
