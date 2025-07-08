package com.github.wallev.maidsoulkitchen.task.cook.beachparty.minifridge;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.beachparty.core.block.entity.MiniFridgeBlockEntity;

@TaskClassAnalyzer(TaskInfo.DBP_MINI_FRIDGE)
public class MiniFridgeCookBe extends CookBeBase<MiniFridgeBlockEntity> {
    public MiniFridgeCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof MiniFridgeBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be;
    }

    @Override
    public int getIngredientSize() {
        return 1;
    }

    @Override
    public int getIngredientSlotStart() {
        return 1;
    }

    @Override
    public int getResultSlot() {
        return 0;
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
