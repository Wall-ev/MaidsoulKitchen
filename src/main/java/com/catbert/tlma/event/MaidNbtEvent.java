package com.catbert.tlma.event;


import com.catbert.tlma.api.IMaidAddon;
import com.catbert.tlma.task.farm.TaskFruitFarm;
import com.catbert.tlma.util.MaidDataUtil;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MaidNbtEvent {

    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (player.getMainHandItem().is(Items.BOOK) && maid.getTask().getUid() == TaskFruitFarm.NAME) {
            if (!maid.level().isClientSide) {
                ((IMaidAddon)maid).setStartYOffset$tlma(((IMaidAddon) maid).getStartYOffset$tlma() + (player.isDiscrete() ? -1 : 1));
                maid.refreshBrain((ServerLevel) maid.level());
            }
            event.setCanceled(true);
        }

    }

}
