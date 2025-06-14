package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.basin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.mao.barbequesdelight.content.block.BasinBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BasinBe extends CookBeBase<BasinBlockEntity> {
    public BasinBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof BasinBlockEntity;
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
        be.notifyTile();
    }
}
