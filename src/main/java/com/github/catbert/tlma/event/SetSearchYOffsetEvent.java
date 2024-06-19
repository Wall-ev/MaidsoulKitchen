package com.github.catbert.tlma.event;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.task.farm.TaskFruitFarm;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class SetSearchYOffsetEvent {

    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (player.getMainHandItem().is(Items.BOOK) && maid.getTask().getUid() == TaskFruitFarm.NAME) {
            if (!maid.level().isClientSide) {

                int startYOffset$tlma = ((IAddonMaid) maid).getStartYOffset$tlma();
                if (!player.isDiscrete() && startYOffset$tlma < 5) {
                    ((IAddonMaid) maid).setStartYOffset$tlma(startYOffset$tlma + 1);
                    maid.refreshBrain((ServerLevel) maid.level());
                } else if (player.isDiscrete() && startYOffset$tlma > -5) {
                    ((IAddonMaid) maid).setStartYOffset$tlma(startYOffset$tlma - 1);
                    maid.refreshBrain((ServerLevel) maid.level());
                } else {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid_addon.book.max_yoffset"));
                }
            }
            event.setCanceled(true);
        }

    }

}