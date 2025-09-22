package com.github.wallev.maidsoulkitchen.compat.msm.common.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import studio.fantasyit.maid_storage_manager.storage.Target;
import studio.fantasyit.maid_storage_manager.storage.base.AbstractFilterableBlockStorage;
import studio.fantasyit.maid_storage_manager.storage.base.IStorageInteractContext;

import java.util.function.Function;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class ContextContainerHandlerView extends AbstractFilterableBlockStorage implements IStorageInteractContext {
    private SimulateInvTargetInteractHelper helper;

    @Override
    public void start(EntityMaid maid, ServerLevel level, Target target) {
        super.start(maid, level,target);
        helper = new SimulateInvTargetInteractHelper(maid, target.getPos(), target.getSide().orElse(null), level);
        helper.open();
    }


    @Override
    public void finish() {
        helper.stop();
    }

    @Override
    public boolean isDone() {
        return helper.doneViewing();
    }

    @Override
    public void reset() {
        helper.reset();
    }

    @Override
    public void tick(Function<ItemStack, ItemStack> process) {
        helper.viewItemTick(process::apply);
    }
}