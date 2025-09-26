package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;
import com.github.wallev.maidsoulkitchen.util.ByteBufUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public record SyncBerryFruitDataMessage(int maidId, ResourceLocation taskId, BerryFruitData berryFruitData) {

    public static void encode(SyncBerryFruitDataMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.maidId());
        buf.writeResourceLocation(message.taskId());

        BerryFruitData berryFruitDataV2 = message.berryFruitData();
        ByteBufUtil.writeMapSB(berryFruitDataV2.rules(), buf);
        buf.writeInt(berryFruitDataV2.searchYOffset());
    }

    public static SyncBerryFruitDataMessage decode(FriendlyByteBuf buf) {
        int maidId = buf.readInt();
        ResourceLocation taskId = buf.readResourceLocation();

        Map<String, Boolean> rules = ByteBufUtil.readMapSB(buf);
        int searchYOffset = buf.readInt();
        return new SyncBerryFruitDataMessage(maidId, taskId, new BerryFruitData(rules, searchYOffset));
    }

    public static void handle(SyncBerryFruitDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                if (sender.level.getEntity(message.maidId) instanceof EntityMaid maid) {
                    TaskManager.findTask(message.taskId).ifPresent(task -> {
                        TaskDataKey<BerryFruitData> cookDataKey = ((ICompatFarmTask<?>) task).getCookDataKey();
                        maid.setAndSyncData(cookDataKey, message.berryFruitData);
                    });
                }
            });
        }
        context.setPacketHandled(true);
    }

}
