package com.github.wallev.maidsoulkitchen.network.packet.s2c;

import com.github.wallev.maidsoulkitchen.client.event.DisplayHubWithMaidRangeEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RenderMaidHubZonePacket(int maidId) {

    public static void encode(RenderMaidHubZonePacket message, FriendlyByteBuf buf) {
        int maidId = message.maidId();
        buf.writeVarInt(maidId);
    }

    public static RenderMaidHubZonePacket decode(FriendlyByteBuf buf) {
        int maidId = buf.readVarInt();
        return new RenderMaidHubZonePacket(maidId);
    }

    public static void handle(RenderMaidHubZonePacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> writeMaidId(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void writeMaidId(RenderMaidHubZonePacket message) {
        DisplayHubWithMaidRangeEvent.add(message.maidId());
    }
}
