package com.github.wallev.maidsoulkitchen.task.cook.copperpot.cooking;

import com.davigj.copperpot.common.block.entity.CopperPotBlockEntity;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

@TaskClassAnalyzer(TaskInfo.COPPER_POT)
public class CopperPotBe extends CookBeBase<CopperPotBlockEntity> {
    public CopperPotBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof CopperPotBlockEntity;
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
        return CopperPotBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public ItemStack getMeal() {
        return be.getMeal();
    }

    @Override
    public ItemStack getNeedContainer() {
        return be.getContainer();
    }

    @Override
    public int getContainerSlot() {
        return CopperPotBlockEntity.CONTAINER_SLOT;
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
    public void markChanged() {
        this.defaultChanged();
    }

    @Override
    public BlockPos getWalkPos() {
        return getPos().below();
    }
}
