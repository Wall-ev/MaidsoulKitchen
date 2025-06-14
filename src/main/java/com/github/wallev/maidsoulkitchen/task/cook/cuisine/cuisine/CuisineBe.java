package com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CuisineBe extends CookBeBase<CuisineSkilletBlockEntity> {
    public CuisineBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof CuisineSkilletBlockEntity;
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
        return be.canCook();
    }

    @Override
    public void markChanged() {
        be.sync();
    }
}
