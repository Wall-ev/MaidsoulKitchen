package com.github.wallev.farmsoulkitchen.network.message;

import com.github.wallev.farmsoulkitchen.item.ItemCookBag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ToggleCookBagGuiSideTabMessage(int tabId) {
    public static void encode(ToggleCookBagGuiSideTabMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.tabId);
    }

    public static ToggleCookBagGuiSideTabMessage decode(FriendlyByteBuf buf) {
        return new ToggleCookBagGuiSideTabMessage(buf.readVarInt());
    }

    public static void handle(ToggleCookBagGuiSideTabMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                ItemCookBag.openCookBagGuiFromSideTab(sender, message.tabId);
            });
        }
        context.setPacketHandled(true);
    }
}