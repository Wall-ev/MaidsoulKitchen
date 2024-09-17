package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleTaskRuleModeMessage2 {
    private final int entityId;
    private final String taskUid;
    private final String mode;

    public ToggleTaskRuleModeMessage2(int entityId, String taskUid, String mode) {
        this.entityId = entityId;
        this.taskUid = taskUid;
        this.mode = mode;
    }

    public static void encode(ToggleTaskRuleModeMessage2 message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeUtf(message.mode);
    }

    public static ToggleTaskRuleModeMessage2 decode(FriendlyByteBuf buf) {
        return new ToggleTaskRuleModeMessage2(buf.readInt(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(ToggleTaskRuleModeMessage2 message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    ((IAddonMaid) entity).setCookTaskMode(message.taskUid, message.mode);
                }
            });
        }
        context.setPacketHandled(true);
    }
}