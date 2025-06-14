package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cuttingboard;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

public class CuttingBoardBe extends CookBeBase<CuttingBoardBlockEntity> {
    public CuttingBoardBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof CuttingBoardBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return null;
    }

    @Override
    public int getIngredientSize() {
        return 0;
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    public boolean recMatch() {
        return false;
    }

    @Override
    public boolean cookStateMatch() {
        return false;
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }
}
