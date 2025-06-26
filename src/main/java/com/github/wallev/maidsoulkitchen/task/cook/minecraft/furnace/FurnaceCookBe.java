package com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

@TaskClassAnalyzer(TaskInfo.FURNACE)
public class FurnaceCookBe extends CookBeBase<AbstractFurnaceBlockEntity> {

    public FurnaceCookBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof AbstractFurnaceBlockEntity;
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
    public int activeItemSlot() {
        return 1;
    }

    @Override
    public int getResultSlot() {
        return 2;
    }

    @Override
    public List<ItemStack> contActiveItemStacks() {
        return AbstractCookingRecSerializerManager.getInstance().getFuels();
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
