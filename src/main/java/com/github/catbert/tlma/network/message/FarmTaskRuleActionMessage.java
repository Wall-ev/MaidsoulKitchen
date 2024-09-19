package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.client.ClientFarmTaskRuleActionMessage;
import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record FarmTaskRuleActionMessage(int entityId, String taskUid, String rule, boolean add) {

    public static void encode(FarmTaskRuleActionMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeUtf(message.rule);
        buf.writeBoolean(message.add);
    }

    public static FarmTaskRuleActionMessage decode(FriendlyByteBuf buf) {
        return new FarmTaskRuleActionMessage(buf.readInt(), buf.readUtf(), buf.readUtf(), buf.readBoolean());
    }

    public static void handle(FarmTaskRuleActionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    if (message.add) {
                        MaidTaskDataUtil.addFarmTaskRule(maid, message.taskUid, message.rule);
                    } else {
                        MaidTaskDataUtil.removeFarmTaskRule(maid, message.taskUid, message.rule);
                    }
                    NetworkHandler.sendToClientPlayer(new ClientFarmTaskRuleActionMessage(message.entityId, message.taskUid, message.rule, message.add), sender);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
