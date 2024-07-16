package com.github.catbert.tlma.network.message;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.inventory.container.ClientTaskSettingMenuManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleSideTabMessage {
    private final int containerId;
    private final int entityId;
    private final int tabId;

    public ToggleSideTabMessage(int containerId, int entityId, int tabId) {
        this.containerId = containerId;
        this.entityId = entityId;
        this.tabId = tabId;
    }

    public static void encode(ToggleSideTabMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.containerId);
        buf.writeInt(message.entityId);
        buf.writeInt(message.tabId);
    }

    public static ToggleSideTabMessage decode(FriendlyByteBuf buf) {
        return new ToggleSideTabMessage(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void handle(ToggleSideTabMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level().getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    ClientTaskSettingMenuManager.setCookTaskData(((IAddonMaid)maid).getCookTaskData1());
                    ClientTaskSettingMenuManager.setMenuData(maid.getPersistentData());
                    ((IAddonMaid) entity).openMaidGuiFromSideTab(sender, message.tabId);
                }
            });
        }
        context.setPacketHandled(true);
    }
}