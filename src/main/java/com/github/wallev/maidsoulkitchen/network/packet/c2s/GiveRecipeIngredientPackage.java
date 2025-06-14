package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record GiveRecipeIngredientPackage(List<ItemStack> list) {

    public static void encode(GiveRecipeIngredientPackage packet, FriendlyByteBuf buf) {
        List<ItemStack> list = packet.list();
        buf.writeVarInt(list.size());
        for (ItemStack itemStack : list) {
            buf.writeItem(itemStack);
        }
    }

    public static GiveRecipeIngredientPackage decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(buf.readItem());
        }
        return new GiveRecipeIngredientPackage(list);
    }

    public static void handle(GiveRecipeIngredientPackage packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                for (ItemStack itemStack : packet.list()) {
                    sender.addItem(itemStack);
                }
            });
        }
        context.setPacketHandled(true);
    }

}
