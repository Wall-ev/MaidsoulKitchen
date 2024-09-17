package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.client.ClientCookTaskRecActionMessage;
import com.github.catbert.tlma.util.MaidAddonTagUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CookTaskRecActionMessage(int entityId, String taskUid, String rec, boolean add) {

    public static void encode(CookTaskRecActionMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeUtf(message.rec);
        buf.writeBoolean(message.add);
    }

    public static CookTaskRecActionMessage decode(FriendlyByteBuf buf) {
        return new CookTaskRecActionMessage(buf.readInt(), buf.readUtf(), buf.readUtf(), buf.readBoolean());
    }

    public static void handle(CookTaskRecActionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
                        MaidAddonTagUtil.addCookTaskRec(maid, message.taskUid, message.rec);
                    } else {
                        MaidAddonTagUtil.removeCookTaskRec(maid, message.taskUid, message.rec);
                    }
                    NetworkHandler.sendToClientPlayer(new ClientCookTaskRecActionMessage(message.entityId, message.taskUid, message.rec, message.add), sender);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
