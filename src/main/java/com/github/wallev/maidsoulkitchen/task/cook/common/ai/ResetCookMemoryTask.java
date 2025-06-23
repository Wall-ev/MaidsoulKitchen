package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.crafting.Recipe;

public class ResetCookMemoryTask<R extends Recipe<? extends Container>> extends Behavior<EntityMaid> implements VBehaviorControl {
    private final MaidCookManager<R> cm;
    public ResetCookMemoryTask(MaidCookManager<R> cm) {
        super(MemoryUtil.getMemoryStateMap(MemoryStatus.REGISTERED));
        this.cm = cm;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityMaid pOwner) {
        return !pOwner.canBrainMoving();
    }

    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        MemoryUtil.resetCookWorkState(pEntity);
        cm.resetState();
    }
}
