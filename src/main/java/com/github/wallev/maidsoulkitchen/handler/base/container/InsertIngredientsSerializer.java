package com.github.wallev.maidsoulkitchen.handler.base.container;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.MkContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InsertIngredientsSerializer<MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookBlockEntitySerializer<MCB, B, R> {
    @Override
    public boolean canDoMaidCookBe(MCB maidCookBe) {
        return this.canInsertIngredients(maidCookBe);
    }

    @Override
    public void doMaidCookBe(MCB maidCookBe) {
        this.insertIngredients(maidCookBe);
    }

    protected boolean canInsertIngredients(MCB maidCookBe) {
        return !maidCookBe.innerCanCook() && !maidCookBe.getRecipesManager().getRecipesIngredients().isEmpty();
    }

    protected void insertIngredients(MCB maidCookBe) {
        MkContainerHelper.insertInputsStack(maidCookBe, maidCookBe.getRecipesManager().getRecipeIngredient());
    }
}
