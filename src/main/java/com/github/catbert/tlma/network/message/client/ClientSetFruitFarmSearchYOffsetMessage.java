package com.github.catbert.tlma.network.message.client;

import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientSetFruitFarmSearchYOffsetMessage(int entityId, String taskUid, int searchYOffset) {

    public static void encode(ClientSetFruitFarmSearchYOffsetMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeInt(message.searchYOffset);
    }

    public static ClientSetFruitFarmSearchYOffsetMessage decode(FriendlyByteBuf buf) {
        return new ClientSetFruitFarmSearchYOffsetMessage(buf.readInt(), buf.readUtf(), buf.readInt());
    }

    public static void handle(ClientSetFruitFarmSearchYOffsetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                LocalPlayer sender = Minecraft.getInstance().player;
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    MaidTaskDataUtil.setFruitFarmSearchYOffset(maid, message.taskUid, message.searchYOffset);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
