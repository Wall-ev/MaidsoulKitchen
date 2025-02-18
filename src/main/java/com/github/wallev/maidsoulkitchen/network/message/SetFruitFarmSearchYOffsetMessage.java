package com.github.wallev.maidsoulkitchen.network.message;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.FruitData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SetFruitFarmSearchYOffsetMessage(int entityId, ResourceLocation dataKey, int searchYOffset) {

    public static void encode(SetFruitFarmSearchYOffsetMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeResourceLocation(message.dataKey);
        buf.writeInt(message.searchYOffset);
    }

    public static SetFruitFarmSearchYOffsetMessage decode(FriendlyByteBuf buf) {
        return new SetFruitFarmSearchYOffsetMessage(buf.readInt(), buf.readResourceLocation(), buf.readInt());
    }

    public static void handle(SetFruitFarmSearchYOffsetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
