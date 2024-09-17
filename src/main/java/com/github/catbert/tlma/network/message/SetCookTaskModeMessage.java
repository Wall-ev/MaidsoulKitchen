package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.client.ClientSetCookTaskModeMessage;
import com.github.catbert.tlma.util.MaidAddonTagUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SetCookTaskModeMessage(int entityId, String taskUid, String mode) {

    public static void encode(SetCookTaskModeMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeUtf(message.mode);
    }

    public static SetCookTaskModeMessage decode(FriendlyByteBuf buf) {
        return new SetCookTaskModeMessage(buf.readInt(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(SetCookTaskModeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    MaidAddonTagUtil.setCookTaskMode(maid, message.taskUid, message.mode);
                    NetworkHandler.sendToClientPlayer(new ClientSetCookTaskModeMessage(message.entityId, message.taskUid, message.mode), sender);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
