package com.github.catbert.tlma.network.message.client;

import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientFarmTaskRuleActionMessage(int entityId, String taskUid, String rule, boolean add) {

    public static void encode(ClientFarmTaskRuleActionMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeUtf(message.rule);
        buf.writeBoolean(message.add);
    }

    public static ClientFarmTaskRuleActionMessage decode(FriendlyByteBuf buf) {
        return new ClientFarmTaskRuleActionMessage(buf.readInt(), buf.readUtf(), buf.readUtf(), buf.readBoolean());
    }

    public static void handle(ClientFarmTaskRuleActionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                LocalPlayer sender = Minecraft.getInstance().player;
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
                }
            });
        }
        context.setPacketHandled(true);
    }
}
