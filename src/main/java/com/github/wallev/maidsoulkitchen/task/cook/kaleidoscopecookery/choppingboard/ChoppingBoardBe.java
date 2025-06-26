package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.choppingboard;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.ysbbbbbb.kaleidoscopecookery.block.entity.ChoppingBoardBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

//@ImportsParse(task = TaskInfo.KC_CHOPPING_BOARD)
public class ChoppingBoardBe extends CookBeBase<ChoppingBoardBlockEntity> {
    public ChoppingBoardBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof ChoppingBoardBlockEntity;
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
        be.refresh();
    }
}
