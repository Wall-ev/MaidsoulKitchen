package com.github.wallev.maidsoulkitchen.network.message;

import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SetCookBagBindModeMessage(String mode) {

    public static void encode(SetCookBagBindModeMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.mode);
    }

    public static SetCookBagBindModeMessage decode(FriendlyByteBuf buf) {
        return new SetCookBagBindModeMessage(buf.readUtf());
    }

    public static void handle(SetCookBagBindModeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
