package com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpan;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.candlelight.core.block.entity.CookingPanBlockEntity;

@TaskClassAnalyzer(TaskInfo.DCL_COOKING_PAN)
public class CookingPanCookBe extends CookBeBase<CookingPanBlockEntity> {
    public CookingPanCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof CookingPanBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be;
    }

    @Override
    public int getIngredientSize() {
        return 7;
    }

    @Override
    public int getResultSlot() {
        return 7;
    }

    @Override
    public boolean recMatch() {
        return this.cookStateMatchAccessor();
    }

    @Override
    public boolean cookStateMatch() {
        return true;
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }
}
