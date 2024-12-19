package com.github.wallev.maidsoulkitchen.network.message;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.BerryData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ActionBerryFarmRuleMessage(int entityId, ResourceLocation dataKey, String rec) {

    public static void encode(ActionBerryFarmRuleMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeResourceLocation(message.dataKey);
        buf.writeUtf(message.rec);
    }

    public static ActionBerryFarmRuleMessage decode(FriendlyByteBuf buf) {
        return new ActionBerryFarmRuleMessage(buf.readInt(), buf.readResourceLocation(), buf.readUtf());
    }

    public static void handle(ActionBerryFarmRuleMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    TaskDataKey<BerryData> value = TaskDataRegister.getValue(message.dataKey);
                    BerryData fruitData = maid.getOrCreateData(value, new BerryData());
                    fruitData.addOrRemoveRule(message.rec);
                    maid.setAndSyncData(value, fruitData);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
