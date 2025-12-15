package com.github.wallev.maidsoulkitchen.task.cook.farmersrespite.kettle;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;

@TaskClassAnalyzer(TaskInfo.FR_KETTLE)
public class KettleCookBe extends CookBeBase<KettleBlockEntity> {
    public KettleCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof KettleBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be.getInventory();
    }

    @Override
    public int getIngredientSize() {
        return 2;
    }

    @Override
    public int getResultSlot() {
        return KettleBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public ItemStack getNeedContainer() {
        return be.getContainer();
    }

    @Override
    public int getContainerSlot() {
        return KettleBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public boolean recMatch() {
        return this.recMatchAccessor();
    }

    @Override
    public boolean cookStateMatch() {
        return be.isHeated();
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
