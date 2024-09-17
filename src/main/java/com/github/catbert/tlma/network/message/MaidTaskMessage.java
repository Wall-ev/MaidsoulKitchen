package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.client.ClientMaidTaskMessage;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MaidTaskMessage(int id, ResourceLocation uid) {
    public static void encode(MaidTaskMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
        buf.writeResourceLocation(message.uid);
    }

    public static MaidTaskMessage decode(FriendlyByteBuf buf) {
        return new MaidTaskMessage(buf.readInt(), buf.readResourceLocation());
    }

    public static void handle(MaidTaskMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    IMaidTask task = TaskManager.findTask(message.uid).orElse(TaskManager.getIdleTask());
                    if (!task.isEnable(maid)) {
                        return;
                    }
                    maid.setTask(task);
                    NetworkHandler.sendToClientPlayer(new ClientMaidTaskMessage(message.id, message.uid), sender);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
