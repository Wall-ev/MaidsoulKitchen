package com.github.wallev.maidsoulkitchen.task.cook.beachparty.palmbar;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.beachparty.core.block.entity.PalmBarBlockEntity;

@TaskClassAnalyzer(TaskInfo.DBP_PALM_BAR)
public class PalmBarCookBe extends CookBeBase<PalmBarBlockEntity> {
    public PalmBarCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof PalmBarBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be;
    }

    @Override
    public int getIngredientSize() {
        return 4;
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
