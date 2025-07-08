package com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.candlelight.core.block.entity.LargeCookingPotBlockEntity;

@TaskClassAnalyzer(TaskInfo.DCL_COOKING_POT)
public class CookingPotCookBe extends CookBeBase<LargeCookingPotBlockEntity> {
    public CookingPotCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof LargeCookingPotBlockEntity;
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
