package com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;

@TaskClassAnalyzer(TaskInfo.BNC_KEY)
public class KegCookBe extends CookBeBase<KegBlockEntity> {
    public KegCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof KegBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be.getInventory();
    }

    @Override
    public int getIngredientSize() {
        return 4;
    }

    @Override
    public int getResultSlot() {
        return KegBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getContainerSlot() {
        return KegBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public boolean recMatch() {
        return this.recMatchAccessor();
    }

    @Override
    public boolean cookStateMatch() {
        return true;
    }

    @Override
    public FluidTank getFluidTank() {
        return be.getFluidTank();
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }
}
