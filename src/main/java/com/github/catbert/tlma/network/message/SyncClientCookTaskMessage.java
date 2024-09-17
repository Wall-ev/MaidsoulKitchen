package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.entity.passive.CookTaskData;
import com.github.tartaricacid.touhoulittlemaid.client.event.MaidAreaRenderEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncMaidAreaMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncClientCookTaskMessage {
    private final int entityId;
    private final String taskUid;
    private final CookTaskData.TaskRule taskRule;

    public SyncClientCookTaskMessage(int entityId, String taskUid, CookTaskData.TaskRule taskRule) {
        this.entityId = entityId;
        this.taskUid = taskUid;
        this.taskRule = taskRule;
    }

    public static void encode(SyncClientCookTaskMessage message, FriendlyByteBuf buf) {
        CookTaskData.TaskRule taskRule = message.taskRule;
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeEnum(taskRule.getMode());
        buf.writeCollection(message.taskRule.getRecipeIds(), FriendlyByteBuf::writeUtf);
    }

    public static SyncClientCookTaskMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        String taskUid = buf.readUtf();
        CookTaskData.Mode mode = buf.readEnum(CookTaskData.Mode.class);
        List<String> recipeIds = buf.readList(FriendlyByteBuf::readUtf);
        CookTaskData.TaskRule taskRule = new CookTaskData.TaskRule(mode, recipeIds);
        return new SyncClientCookTaskMessage(entityId, taskUid, taskRule);
    }

    public static void handle(SyncClientCookTaskMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender) && maid instanceof IAddonMaid addonMaid) {
                    addonMaid.setTaskRule(message.taskUid, message.taskRule);
                }
            });
        }
        context.setPacketHandled(true);
    }

//    public static void handle(SyncClientCookTaskMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getReceptionSide().isClient()) {
//            context.enqueueWork(() -> writePos(message));
//        }
//        context.setPacketHandled(true);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private static void writePos(SyncMaidAreaMessage message) {
//
//    }
}
