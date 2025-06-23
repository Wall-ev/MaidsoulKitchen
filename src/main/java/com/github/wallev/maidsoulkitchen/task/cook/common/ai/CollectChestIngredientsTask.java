package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.debug.annotation.SafeRun;
import com.github.wallev.maidsoulkitchen.debug.annotation.TimeRecord;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.crafting.Recipe;

public class CollectChestIngredientsTask<R extends Recipe<? extends Container>> extends Behavior<EntityMaid> implements VBehaviorControl {
    private final MaidCookManager<R> rm;
    public CollectChestIngredientsTask(MaidCookManager<R> rm) {
        super(ImmutableMap.of(MkEntities.CET_CHEST_ITEMHANDLER.get(), MemoryStatus.VALUE_PRESENT));
        this.rm = rm;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityMaid pOwner) {
        return rm.getRunState() == 1;
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        MutableComponent append = VComponent.literal("让我来看看仓库都有什么材料~");
        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(append);
        pEntity.getChatBubbleManager().addChatBubble(textChatBubbleData);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        return !rm.getChestInputInventory().done();
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void tick(ServerLevel pLevel, EntityMaid pOwner, long pGameTime) {
        rm.getChestInputInventory().tickScan();
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        MemoryUtil.eraseCollectChestItemHandler(pEntity);
        rm.startGenerateRecs();
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }
}
