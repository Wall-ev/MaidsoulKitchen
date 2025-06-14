package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClearCookBagBindPosesPackage() {

    public static void encode(ClearCookBagBindPosesPackage message, FriendlyByteBuf buf) {
    }

    public static ClearCookBagBindPosesPackage decode(FriendlyByteBuf buf) {
        return new ClearCookBagBindPosesPackage();
    }

    public static void handle(ClearCookBagBindPosesPackage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
