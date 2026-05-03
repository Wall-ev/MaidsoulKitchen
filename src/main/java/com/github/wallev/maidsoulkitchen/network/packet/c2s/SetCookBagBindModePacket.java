package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SetCookBagBindModePacket(String mode) {

    public static void encode(SetCookBagBindModePacket message, FriendlyByteBuf buf) {
        buf.writeUtf(message.mode);
    }

    public static SetCookBagBindModePacket decode(FriendlyByteBuf buf) {
        return new SetCookBagBindModePacket(buf.readUtf());
    }

    public static void handle(SetCookBagBindModePacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                ItemCulinaryHub.setBindModeTag(sender.getMainHandItem(), message.mode);
            });
        }
        context.setPacketHandled(true);
    }
}
