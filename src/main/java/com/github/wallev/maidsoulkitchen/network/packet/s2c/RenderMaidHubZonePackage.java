package com.github.wallev.maidsoulkitchen.network.packet.s2c;

import com.github.wallev.maidsoulkitchen.client.event.DisplayHubWithMaidRangeEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RenderMaidHubZonePackage(int maidId) {

    public static void encode(RenderMaidHubZonePackage message, FriendlyByteBuf buf) {
        int maidId = message.maidId();
        buf.writeVarInt(maidId);
    }

    public static RenderMaidHubZonePackage decode(FriendlyByteBuf buf) {
        int maidId = buf.readVarInt();
        return new RenderMaidHubZonePackage(maidId);
    }

    public static void handle(RenderMaidHubZonePackage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> writeMaidId(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void writeMaidId(RenderMaidHubZonePackage message) {
        DisplayHubWithMaidRangeEvent.add(message.maidId());
    }
}
