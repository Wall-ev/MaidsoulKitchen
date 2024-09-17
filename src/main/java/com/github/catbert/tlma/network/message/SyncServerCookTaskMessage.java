package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.entity.passive.CookTaskData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncServerCookTaskMessage {
    private final int entityId;
    private final String taskUid;
    private final CookTaskData.TaskRule taskRule;

    public SyncServerCookTaskMessage(int entityId, String taskUid, CookTaskData.TaskRule taskRule) {
        this.entityId = entityId;
        this.taskUid = taskUid;
        this.taskRule = taskRule;
    }

    public static void encode(SyncServerCookTaskMessage message, FriendlyByteBuf buf) {

        CookTaskData.TaskRule taskRule = message.taskRule;
        buf.writeInt(message.entityId);
        buf.writeUtf(message.taskUid);
        buf.writeEnum(taskRule.getMode());
        buf.writeCollection(message.taskRule.getRecipeIds(), FriendlyByteBuf::writeUtf);
    }

    public static SyncServerCookTaskMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        String taskUid = buf.readUtf();
        CookTaskData.Mode mode = buf.readEnum(CookTaskData.Mode.class);
        List<String> recipeIds = buf.readList(FriendlyByteBuf::readUtf);
        CookTaskData.TaskRule taskRule = new CookTaskData.TaskRule(mode, recipeIds);
        return new SyncServerCookTaskMessage(entityId, taskUid, taskRule);
    }

    public static void handle(SyncServerCookTaskMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
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
}
