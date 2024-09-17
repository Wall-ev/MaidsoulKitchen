package com.github.catbert.tlma.network.message.client;

import com.github.catbert.tlma.util.MaidAddonTagUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientCookTaskRecActionMessage(int entityId, String taskUid, String rec, boolean add) {

    public static void encode(ClientCookTaskRecActionMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeUtf(message.rec);
        buf.writeBoolean(message.add);
    }

    public static ClientCookTaskRecActionMessage decode(FriendlyByteBuf buf) {
        return new ClientCookTaskRecActionMessage(buf.readInt(), buf.readUtf(), buf.readUtf(), buf.readBoolean());
    }

    public static void handle(ClientCookTaskRecActionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
                        MaidAddonTagUtil.addCookTaskRec(maid, message.taskUid, message.rec);
                    } else {
                        MaidAddonTagUtil.removeCookTaskRec(maid, message.taskUid, message.rec);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
