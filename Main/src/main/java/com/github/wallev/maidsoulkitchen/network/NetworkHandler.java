package com.github.wallev.maidsoulkitchen.network;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.network.packet.c2s.*;
import com.github.wallev.maidsoulkitchen.network.packet.s2c.RenderMaidHubZonePacket;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.Optional;

public final class NetworkHandler {
    private static final String VERSION = "1.0.0";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(VResourceLocation.of(MaidsoulKitchen.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        int i = 0;
        // Server
        CHANNEL.registerMessage(i++, ToggleCookBagGuiSideTabPacket.class, ToggleCookBagGuiSideTabPacket::encode, ToggleCookBagGuiSideTabPacket::decode, ToggleCookBagGuiSideTabPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookBagBindModePacket.class, SetCookBagBindModePacket::encode, SetCookBagBindModePacket::decode, SetCookBagBindModePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookDataModePacket.class, SetCookDataModePacket::encode, SetCookDataModePacket::decode, SetCookDataModePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionCookDataRecPacket.class, ActionCookDataRecPacket::encode, ActionCookDataRecPacket::decode, ActionCookDataRecPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionCookDataRecsPacket.class, ActionCookDataRecsPacket::encode, ActionCookDataRecsPacket::decode, ActionCookDataRecsPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetFruitFarmSearchYOffsetPacket.class, SetFruitFarmSearchYOffsetPacket::encode, SetFruitFarmSearchYOffsetPacket::decode, SetFruitFarmSearchYOffsetPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionBerryFarmRulePacket.class, ActionBerryFarmRulePacket::encode, ActionBerryFarmRulePacket::decode, ActionBerryFarmRulePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionFruitFarmRulePacket.class, ActionFruitFarmRulePacket::encode, ActionFruitFarmRulePacket::decode, ActionFruitFarmRulePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClearCookBagBindPosesPacket.class, ClearCookBagBindPosesPacket::encode, ClearCookBagBindPosesPacket::decode, ClearCookBagBindPosesPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, GiveRecipeIngredientPacket.class, GiveRecipeIngredientPacket::encode, GiveRecipeIngredientPacket::decode, GiveRecipeIngredientPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SyncBerryFruitDataMessage.class, SyncBerryFruitDataMessage::encode, SyncBerryFruitDataMessage::decode, SyncBerryFruitDataMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SyncKitchenDataC2SMessage.class, SyncKitchenDataC2SMessage::encode, SyncKitchenDataC2SMessage::decode, SyncKitchenDataC2SMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        // Server && Client

        // Client
        CHANNEL.registerMessage(i++, RenderMaidHubZonePacket.class, RenderMaidHubZonePacket::encode, RenderMaidHubZonePacket::decode, RenderMaidHubZonePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendToClientPlayer(Object message, Player player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

    public static class C2S {
        public static void toggleCookBagGuiSideTab(int tabId) {
            sendToServer(new ToggleCookBagGuiSideTabPacket(tabId));
        }

        public static void setCookBagBindMode(String mode) {
            sendToServer(new SetCookBagBindModePacket(mode));
        }

        public static void setCookDataMode(int entityId, ResourceLocation dataKey, String mode) {
            sendToServer(new SetCookDataModePacket(entityId, dataKey, mode));
        }

        public static void actionCookDataRec(int entityId, ResourceLocation dataKey, String rec, String mode) {
            sendToServer(new ActionCookDataRecPacket(entityId, dataKey, rec, mode));
        }

        public static void actionCookDataRecs(int entityId, ResourceLocation dataKey, List<String> rec, boolean add) {
            sendToServer(new ActionCookDataRecsPacket(entityId, dataKey, rec, add));
        }

        public static void setFruitFarmSearchYOffset(int entityId, ResourceLocation dataKey, int searchYOffset) {
            sendToServer(new SetFruitFarmSearchYOffsetPacket(entityId, dataKey, searchYOffset));
        }

        public static void actionBerryFarmRule(int entityId, ResourceLocation dataKey, String rec) {
            sendToServer(new ActionBerryFarmRulePacket(entityId, dataKey, rec));
        }

        public static void actionFruitFarmRule(int entityId, ResourceLocation dataKey, String rec) {
            sendToServer(new ActionFruitFarmRulePacket(entityId, dataKey, rec));
        }

        public static void clearCookBagBindPoses() {
            sendToServer(new ClearCookBagBindPosesPacket());
        }

        public static void giveRecipeIngredient(List<ItemStack> itemStacks) {
            sendToServer(new GiveRecipeIngredientPacket(itemStacks));
        }

        public static void syncBerryFruitData(int maidId, ResourceLocation taskId, BerryFruitData data) {
            NetworkHandler.sendToServer(new SyncBerryFruitDataMessage(maidId, taskId, data));
        }

        public static void syncKitchenData2(int maidId, KitchenData data) {
            NetworkHandler.sendToServer(new SyncKitchenDataC2SMessage(maidId, data));
        }
    }

    public static class S2C {
        public static void renderMaidHubZone(int maidId, Player player) {
            sendToClientPlayer(new RenderMaidHubZonePacket(maidId), player);
        }
    }

    public static class SAC {

    }
}
