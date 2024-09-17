package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ToggleSideTabMessage(int containerId, int entityId, int tabId, ResourceLocation taskUid, boolean taskListOpen, int taskPage) {

    public static void encode(ToggleSideTabMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.containerId);
        buf.writeInt(message.entityId);
        buf.writeInt(message.tabId);
        buf.writeResourceLocation(message.taskUid);
        buf.writeBoolean(message.taskListOpen);
        buf.writeInt(message.taskPage);
    }

    public static ToggleSideTabMessage decode(FriendlyByteBuf buf) {
        return new ToggleSideTabMessage(buf.readInt(), buf.readInt(), buf.readInt(), buf.readResourceLocation(), buf.readBoolean(), buf.readInt());
    }

    public static void handle(ToggleSideTabMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    ((IAddonMaid) entity).openMaidGuiFromSideTab(sender, message.tabId, message.taskListOpen, message.taskPage);
                }
            });
        }
        context.setPacketHandled(true);
    }
}