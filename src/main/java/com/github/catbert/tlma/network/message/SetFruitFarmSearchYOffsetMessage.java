package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.client.ClientSetFruitFarmSearchYOffsetMessage;
import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SetFruitFarmSearchYOffsetMessage(int entityId, String taskUid, int searchYOffset) {

    public static void encode(SetFruitFarmSearchYOffsetMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeInt(message.searchYOffset);
    }

    public static SetFruitFarmSearchYOffsetMessage decode(FriendlyByteBuf buf) {
        return new SetFruitFarmSearchYOffsetMessage(buf.readInt(), buf.readUtf(), buf.readInt());
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
                    MaidTaskDataUtil.setFruitFarmSearchYOffset(maid, message.taskUid, message.searchYOffset);
                    maid.refreshBrain((ServerLevel) maid.level());
                    NetworkHandler.sendToClientPlayer(new ClientSetFruitFarmSearchYOffsetMessage(message.entityId, message.taskUid, message.searchYOffset), sender);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
