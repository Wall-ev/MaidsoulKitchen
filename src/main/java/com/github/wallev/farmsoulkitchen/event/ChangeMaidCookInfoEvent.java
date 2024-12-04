package com.github.wallev.farmsoulkitchen.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.InitItems;
import com.github.wallev.farmsoulkitchen.network.NetworkHandler;
import com.github.wallev.farmsoulkitchen.network.message.SetCookDataModeMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FarmsoulKitchen.MOD_ID)
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
