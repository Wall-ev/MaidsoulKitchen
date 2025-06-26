package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cookingpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@TaskClassAnalyzer(TaskInfo.FD_COOK_POT)
public class CookingPotBe extends CookBeBase<CookingPotBlockEntity> {
    public CookingPotBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof CookingPotBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be.getInventory();
    }

    @Override
    public int getIngredientSize() {
        return 6;
    }

    @Override
    public int getResultSlot() {
        return CookingPotBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public ItemStack getMeal() {
        return be.getMeal();
    }

    @Override
    public ItemStack getNeedContainer() {
        return be.getContainer();
    }

    @Override
    public int getContainerSlot() {
        return CookingPotBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public boolean recMatch() {
        return this.recMatchAccessor();
    }

    @Override
    public boolean cookStateMatch() {
        return be.isHeated();
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }
}
