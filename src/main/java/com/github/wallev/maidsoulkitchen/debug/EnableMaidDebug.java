package com.github.wallev.maidsoulkitchen.debug;

import com.github.tartaricacid.touhoulittlemaid.config.ServerConfig;
import com.github.tartaricacid.touhoulittlemaid.debug.target.DebugMaidManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * 开启 TLM 调试
 */
public class EnableMaidDebug {

    // 路径开启
    @SubscribeEvent
    public void pathing(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof EntityMaid maid) {
            LivingEntity owner = maid.getOwner();
            if (owner != null && owner.isAlive()) {
                if (!DebugMaidManager.getDebuggingPlayer(maid).contains((ServerPlayer)owner)) {
                    DebugMaidManager.triggerDebuggingMaid((ServerPlayer) owner, maid);
                }
            }
        }
    }

    // 路径开启
    @SubscribeEvent
    public void pathing(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer && serverPlayer.level instanceof ServerLevel serverLevel)) {
            return;
        }

        List<EntityMaid> debuggingMaids = DebugMaidManager.getDebuggingMaid(serverPlayer);
        serverLevel.getEntities().getAll()
            .forEach(entity -> {
                if (entity instanceof EntityMaid maid && !debuggingMaids.contains(maid)) {
                    DebugMaidManager.triggerDebuggingMaid(serverPlayer, maid);
                }
            });
    }

    // AI超时检测
    @SubscribeEvent
    public void aiTime(PlayerEvent.PlayerLoggedInEvent event) {
        ServerConfig.MAID_AI_TIME_DEBUG.set(true);
    }

    // 好感度满级
    @SubscribeEvent
    public void maxFavorability(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof EntityMaid maid) {
            LivingEntity owner = maid.getOwner();
            if (owner != null) {
                maid.getFavorabilityManager().max();
            }
        }
    }
}
