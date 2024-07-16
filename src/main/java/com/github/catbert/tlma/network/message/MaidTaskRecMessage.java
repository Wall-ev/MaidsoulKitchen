package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.client.gui.entity.maid.cook.CookSettingContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidTaskRecMessage {
    private final int entityId;
    private final String recipeId;

    public MaidTaskRecMessage(int entityId, String recipeId) {
        this.entityId = entityId;
        this.recipeId = recipeId;
    }

    public static void encode(MaidTaskRecMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.recipeId);
    }

    public static MaidTaskRecMessage decode(FriendlyByteBuf buf) {
        return new MaidTaskRecMessage(buf.readInt(), buf.readUtf());
    }

    public static void handle(MaidTaskRecMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level().getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
//                    ((IAddonMaid) entity).addOrRemoveRecipe(message.recipeId);
//
                    ((IAddonMaid) entity).addOrRemoveRecipe1(message.recipeId);

//                    ((IAddonMaid) entity).addOrRemoveRecipe2(message.recipeId);
                }
            });
        }
        context.setPacketHandled(true);
    }
}