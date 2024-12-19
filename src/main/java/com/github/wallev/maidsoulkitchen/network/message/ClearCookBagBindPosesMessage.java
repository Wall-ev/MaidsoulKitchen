package com.github.wallev.maidsoulkitchen.network.message;

import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClearCookBagBindPosesMessage() {

    public static void encode(ClearCookBagBindPosesMessage message, FriendlyByteBuf buf) {
    }

    public static ClearCookBagBindPosesMessage decode(FriendlyByteBuf buf) {
        return new ClearCookBagBindPosesMessage();
    }

    public static void handle(ClearCookBagBindPosesMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                ItemStack mainHandItem = sender.getMainHandItem();
                ItemCulinaryHub.removeModePoses(mainHandItem);
            });
        }
        context.setPacketHandled(true);
    }
}
