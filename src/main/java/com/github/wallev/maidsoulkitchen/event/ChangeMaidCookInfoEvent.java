package com.github.wallev.maidsoulkitchen.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID)
public class ChangeMaidCookInfoEvent {
    @SubscribeEvent
    public static void copyEntityId(PlayerInteractEvent.EntityInteract event) {
//        Player player = event.getEntity();
//        InteractionHand hand = event.getHand();
//        Entity target = event.getTarget();
//        if (target instanceof EntityMaid maid && maid.getTask() instanceof ICookTask<?,?> cookTask && player.getItemInHand(hand).is(InitItems.BURN_PROTECT_BAUBLE.get())) {
////            NetworkHandler.sendToServer(new SetCookDataModeMessage(maid.getId(), cookTask.getCookDataKey().getKey(), CookData.Mode.SELECT.name));
//            CookData data = maid.getData(cookTask.getCookDataKey());
//            if (data != null) {
//                data.setMode(CookData.Mode.SELECT.name);
//            }
//            event.setCanceled(true);
//        }
    }
}
