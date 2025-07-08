package com.github.wallev.maidsoulkitchen.network.packet.c2s;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.CookDataV1;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public record SyncKitchenDataC2SMessage(int maidId, KitchenData kitchenData) {

    public static void encode(SyncKitchenDataC2SMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.maidId());

        KitchenData kitchenData = message.kitchenData();
        buf.writeResourceLocation(kitchenData.getCookName());

        buf.writeVarInt(kitchenData.getCookDatas().size());
        kitchenData.getCookDatas().forEach((typeName, cookData) -> {
            buf.writeResourceLocation(typeName);
            encodeCookData(cookData, buf);
        });
    }

    public static SyncKitchenDataC2SMessage decode(FriendlyByteBuf buf) {
        int maidId = buf.readInt();

        ResourceLocation kitchenName = buf.readResourceLocation();

        Map<ResourceLocation, CookDataV1> cookDataMap = Maps.newHashMap();
        int cookDataSize = buf.readVarInt();
        for (int i = 0; i < cookDataSize; i++) {
            ResourceLocation typeName = buf.readResourceLocation();
            CookDataV1 cookData = decodeCookData(buf);

            cookDataMap.put(typeName, cookData);
        }

        return new SyncKitchenDataC2SMessage(maidId, new KitchenData(cookDataMap, kitchenName));
    }

    private static void encodeCookData(CookDataV1 cookData, FriendlyByteBuf buf) {
        buf.writeUtf(cookData.mode());

        buf.writeVarInt(cookData.whitelistRecs().size());
        cookData.whitelistRecs().forEach(buf::writeUtf);

        buf.writeVarInt(cookData.blacklistRecs().size());
        cookData.blacklistRecs().forEach(buf::writeUtf);
    }

    private static CookDataV1 decodeCookData(FriendlyByteBuf buf) {
        String mode = buf.readUtf();

        int whiteListSize = buf.readVarInt();
        List<String> whiteList = Lists.newArrayList();
        for (int i = 0; i < whiteListSize; i++) {
            String whiteRec = buf.readUtf();
            whiteList.add(whiteRec);
        }

        int blackListSize = buf.readVarInt();
        List<String> blackList = Lists.newArrayList();
        for (int i = 0; i < blackListSize; i++) {
            String blackRec = buf.readUtf();
            blackList.add(blackRec);
        }

        return new CookDataV1(mode, whiteList, blackList);
    }

    public static void handle(SyncKitchenDataC2SMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.maidId());
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    TaskDataKey<KitchenData> kitchenDataTaskDataKey2 = TaskDataRegister.getValue(DataRegister.COOK.getKey());
                    maid.setAndSyncData(kitchenDataTaskDataKey2, message.kitchenData());
                }
            });
        }
        context.setPacketHandled(true);
    }
}
