package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v0.CookData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ActionCookDataRecsPacket(int entityId, ResourceLocation dataKey, List<String> recs, boolean add) {

    public static void encode(ActionCookDataRecsPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeResourceLocation(message.dataKey);

        List<String> recs = message.recs();
        int size = recs.size();
        buf.writeVarInt(size);
        for (String rec : recs) {
            buf.writeUtf(rec);
        }

        buf.writeBoolean(message.add);
    }

    public static ActionCookDataRecsPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        ResourceLocation dataKey = buf.readResourceLocation();

        int size = buf.readVarInt();
        List<String> recs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            recs.add(buf.readUtf());
        }

        boolean add = buf.readBoolean();
        return new ActionCookDataRecsPacket(entityId, dataKey, recs, add);
    }

    public static void handle(ActionCookDataRecsPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    TaskDataKey<CookData> value = TaskDataRegister.getValue(message.dataKey);
                    CookData cookData = maid.getOrCreateData(value, new CookData());

                    boolean add = message.add();
                    List<String> recs = message.recs();
                    if (add) {
                        cookData.addRecs(recs);
                    } else {
                        cookData.removeRecs(recs);
                    }

                    maid.setAndSyncData(value, cookData);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
