package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v0.FruitData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SetFruitFarmSearchYOffsetPacket(int entityId, ResourceLocation dataKey, int searchYOffset) {

    public static void encode(SetFruitFarmSearchYOffsetPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeResourceLocation(message.dataKey);
        buf.writeInt(message.searchYOffset);
    }

    public static SetFruitFarmSearchYOffsetPacket decode(FriendlyByteBuf buf) {
        return new SetFruitFarmSearchYOffsetPacket(buf.readInt(), buf.readResourceLocation(), buf.readInt());
    }

    public static void handle(SetFruitFarmSearchYOffsetPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    TaskDataKey<FruitData> value = TaskDataRegister.getValue(message.dataKey);
                    FruitData fruitData = maid.getOrCreateData(value, new FruitData());
                    fruitData.setSearchYOffset(message.searchYOffset);
                    maid.setAndSyncData(value, fruitData);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
