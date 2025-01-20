package com.github.wallev.maidsoulkitchen.handler.base.container;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.IOutputAddition;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.MkContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InsertMealContainerSerializer<MCB extends AbstractMaidCookBe<B, R> & IOutputAddition.NeedOutputContainer,
        B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookBlockEntitySerializer<MCB, B, R> {
    @Override
    public boolean canDoMaidCookBe(MCB maidCookBe) {
        return !this.hasMealStack(maidCookBe) && !this.isMealContainerItem(maidCookBe)
                && maidCookBe.getRecipesManager().hasOutputAdditionItem(maidCookBe.getMealContainerItem());
    }

    @Override
    public void doMaidCookBe(MCB maidCookBe) {
        ItemStack outputAdditionItem = maidCookBe.getRecipesManager().findOutputAdditionItem(maidCookBe.getMealContainerItem());
        if (!outputAdditionItem.isEmpty()) {
            MkContainerHelper.insertItemInSlot(maidCookBe, maidCookBe.getOutputContainerSlot(), outputAdditionItem);
        }
    }

    protected boolean hasMealStack(MCB maidCookBe) {
        return maidCookBe.getOutputMealStack().isEmpty();
    }

    protected boolean isMealContainerItem(MCB maidCookBe) {
        ItemStack mealContainerItem = maidCookBe.getMealContainerItem();
        return maidCookBe.getOutputContainerStack().is(mealContainerItem.getItem());
    }
}
