package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ToggleCookBagGuiSideTabPacket(int tabId) {
    public static void encode(ToggleCookBagGuiSideTabPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.tabId);
    }

    public static ToggleCookBagGuiSideTabPacket decode(FriendlyByteBuf buf) {
        return new ToggleCookBagGuiSideTabPacket(buf.readVarInt());
    }

    public static void handle(ToggleCookBagGuiSideTabPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                ItemCulinaryHub.openCookBagGuiFromSideTab(sender, message.tabId);
            });
        }
        context.setPacketHandled(true);
    }
}