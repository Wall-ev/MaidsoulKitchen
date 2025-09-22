package com.github.wallev.maidsoulkitchen.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID)
public class MaidHubClickEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (player.isDiscrete() && player.getMainHandItem().is(ModItems.CULINARY_HUB.get())) {
            if (!maid.level.isClientSide) {
                NetworkHandler.S2C.renderMaidHubZone(maid.getId(), player);
            }
            event.setCanceled(true);
        }
    }
}
