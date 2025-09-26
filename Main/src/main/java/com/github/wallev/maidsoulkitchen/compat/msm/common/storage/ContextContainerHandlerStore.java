package com.github.wallev.maidsoulkitchen.compat.msm.common.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import studio.fantasyit.maid_storage_manager.storage.Target;
import studio.fantasyit.maid_storage_manager.storage.base.AbstractFilterableBlockStorage;
import studio.fantasyit.maid_storage_manager.storage.base.IStorageInsertableContext;
import studio.fantasyit.maid_storage_manager.storage.base.IStorageSplitInsertableContext;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class ContextContainerHandlerStore extends AbstractFilterableBlockStorage implements IStorageInsertableContext, IStorageSplitInsertableContext {
    private SimulateInvTargetInteractHelper helper;


    @Override
    public void start(EntityMaid maid, ServerLevel level, Target target) {
        super.start(maid, level,target);
        helper = new SimulateInvTargetInteractHelper(maid, target.pos, target.side, level);
        helper.open();
    }

    @Override
    public void finish() {
        helper.stop();
    }

    @Override
    public ItemStack insert(ItemStack item) {
        if (!this.helper.isStillValid()) return item;
        ItemStack copy = item.copy();
        for (int i = 0; i < this.helper.iInvHandler.kl$getSlots(); i++) {
            copy = this.helper.iInvHandler.kl$insertItem(i, copy, false);
            if (copy.isEmpty()) return ItemStack.EMPTY;
        }
        return copy;
    }

    @Override
    public ItemStack splitInsert(ItemStack item) {
        if (!this.isAvailable(item)) return item;
        if (!this.helper.isStillValid()) return item;
        ItemStack copy = item.copy();
        for (int i = 0; i < this.helper.iInvHandler.kl$getSlots(); i++) {
            if(!this.helper.iInvHandler.kl$getStackInSlot(i).isEmpty())
                continue;
            copy = this.helper.iInvHandler.kl$insertItem(i, copy, false);
            if (copy.isEmpty()) return ItemStack.EMPTY;
        }
        return copy;
    }
}