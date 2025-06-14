package com.github.wallev.maidsoulkitchen.network;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.network.packet.c2s.*;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
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

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(VResourceLocation.create(MaidsoulKitchen.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        int i = 0;
        // Server
        CHANNEL.registerMessage(i++, ToggleCookBagGuiSideTabPackage.class, ToggleCookBagGuiSideTabPackage::encode, ToggleCookBagGuiSideTabPackage::decode, ToggleCookBagGuiSideTabPackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookBagBindModePackage.class, SetCookBagBindModePackage::encode, SetCookBagBindModePackage::decode, SetCookBagBindModePackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetCookDataModePackage.class, SetCookDataModePackage::encode, SetCookDataModePackage::decode, SetCookDataModePackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionCookDataRecPackage.class, ActionCookDataRecPackage::encode, ActionCookDataRecPackage::decode, ActionCookDataRecPackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, SetFruitFarmSearchYOffsetPackage.class, SetFruitFarmSearchYOffsetPackage::encode, SetFruitFarmSearchYOffsetPackage::decode, SetFruitFarmSearchYOffsetPackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionBerryFarmRulePackage.class, ActionBerryFarmRulePackage::encode, ActionBerryFarmRulePackage::decode, ActionBerryFarmRulePackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ActionFruitFarmRulePackage.class, ActionFruitFarmRulePackage::encode, ActionFruitFarmRulePackage::decode, ActionFruitFarmRulePackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, ClearCookBagBindPosesPackage.class, ClearCookBagBindPosesPackage::encode, ClearCookBagBindPosesPackage::decode, ClearCookBagBindPosesPackage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(i++, GiveRecipeIngredientPackage.class, GiveRecipeIngredientPackage::encode, GiveRecipeIngredientPackage::decode, GiveRecipeIngredientPackage::handle,
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

    public static class C2S {
        public static void toggleCookBagGuiSideTab(int tabId) {
            sendToServer(new ToggleCookBagGuiSideTabPackage(tabId));
        }

        public static void setCookBagBindMode(String mode) {
            sendToServer(new SetCookBagBindModePackage(mode));
        }

        public static void setCookDataMode(int entityId, ResourceLocation dataKey, String mode) {
            sendToServer(new SetCookDataModePackage(entityId, dataKey, mode));
        }

        public static void actionCookDataRec(int entityId, ResourceLocation dataKey, String rec, String mode) {
            sendToServer(new ActionCookDataRecPackage(entityId, dataKey, rec, mode));
        }

        public static void setFruitFarmSearchYOffset(int entityId, ResourceLocation dataKey, int searchYOffset) {
            sendToServer(new SetFruitFarmSearchYOffsetPackage(entityId, dataKey, searchYOffset));
        }

        public static void actionBerryFarmRule(int entityId, ResourceLocation dataKey, String rec) {
            sendToServer(new ActionBerryFarmRulePackage(entityId, dataKey, rec));
        }

        public static void actionFruitFarmRule(int entityId, ResourceLocation dataKey, String rec) {
            sendToServer(new ActionFruitFarmRulePackage(entityId, dataKey, rec));
        }

        public static void clearCookBagBindPoses() {
            sendToServer(new ClearCookBagBindPosesPackage());
        }

        public static void giveRecipeIngredient(List<ItemStack> itemStacks) {
            sendToServer(new GiveRecipeIngredientPackage(itemStacks));
        }
    }

    public static class S2C {

    }

    public static class SAC {

    }
}
