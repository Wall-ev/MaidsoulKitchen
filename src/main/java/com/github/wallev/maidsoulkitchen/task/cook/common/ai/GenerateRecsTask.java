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

public class GenerateRecsTask<R extends Recipe<? extends Container>> extends Behavior<EntityMaid> implements VBehaviorControl {
    private final MaidCookManager<R> cm;

    public GenerateRecsTask(MaidCookManager<R> cm) {
        super(ImmutableMap.of(MkEntities.GENERATE_RECS.get(), MemoryStatus.VALUE_PRESENT));
        this.cm = cm;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityMaid pOwner) {
        return cm.getRunState() == 2;
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        MutableComponent append = VComponent.literal("让我来看看都可以制作那些食物~");
        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(append);
        pEntity.getChatBubbleManager().addChatBubble(textChatBubbleData);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        return !cm.recsGenerateDone();
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void tick(ServerLevel pLevel, EntityMaid pOwner, long pGameTime) {
        cm.tickGenerateRecs();
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        MemoryUtil.eraseGenerateRecs(pEntity);
        cm.recsGenDoneAndUpdate();
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }
}
