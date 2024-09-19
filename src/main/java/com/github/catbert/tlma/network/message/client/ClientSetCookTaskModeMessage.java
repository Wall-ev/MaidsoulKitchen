package com.github.catbert.tlma.network.message.client;

import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientSetCookTaskModeMessage(int entityId, String taskUid, String mode) {

    public static void encode(ClientSetCookTaskModeMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeUtf(message.mode);
    }

    public static ClientSetCookTaskModeMessage decode(FriendlyByteBuf buf) {
        return new ClientSetCookTaskModeMessage(buf.readInt(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(ClientSetCookTaskModeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                LocalPlayer sender = Minecraft.getInstance().player;
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    MaidTaskDataUtil.setCookTaskMode(maid, message.taskUid, message.mode);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
