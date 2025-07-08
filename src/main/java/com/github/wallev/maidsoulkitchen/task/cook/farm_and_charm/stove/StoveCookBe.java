package com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.stove;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.farm_and_charm.core.block.entity.StoveBlockEntity;

import java.util.List;

@TaskClassAnalyzer(TaskInfo.DFC_STOVE)
public class StoveCookBe extends CookBeBase<StoveBlockEntity> {

    public StoveCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof StoveBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be;
    }

    @Override
    public int getIngredientSlotStart() {
        return 1;
    }

    @Override
    public int getIngredientSize() {
        return 3;
    }

    @Override
    public int activeItemSlot() {
        return 4;
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    public List<ItemStack> contActiveItemStacks() {
        return StoveRecSerializerManager.getInstance().getFuels();
    }

    @Override
    public boolean recMatch() {
        return this.recMatchAccessor();
    }

    @Override
    public boolean cookStateMatch() {
        return this.cookStateMatchAccessor();
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }
}
