package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleTaskRuleModeMessage {
    private final int entityId;
//    private final String rec;

    public ToggleTaskRuleModeMessage(int entityId) {
        this.entityId = entityId;
//        this.rec = rec;
    }

    public static void encode(ToggleTaskRuleModeMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
//        buf.writeUtf(message.rec);
    }

    public static ToggleTaskRuleModeMessage decode(FriendlyByteBuf buf) {
        return new ToggleTaskRuleModeMessage(buf.readInt());
    }

    public static void handle(ToggleTaskRuleModeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    ((IAddonMaid) entity).toggleTaskRuleMode1();
                }
            });
        }
        context.setPacketHandled(true);
    }
}