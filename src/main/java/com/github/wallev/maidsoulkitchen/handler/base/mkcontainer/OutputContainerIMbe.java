package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class OutputContainerIMbe<B extends BlockEntity, R extends Recipe<? extends Container>> extends ItemHandlerMaidCookBe<B, R> implements IOutputAddition.NeedOutputContainer {
    // @final
    private int outputContainerSlot;
    // @final
    private int outputMealSlot;
    public OutputContainerIMbe(EntityMaid maid, MaidRecipesManager<R> recipesManager, B cookBe) {
        super(maid, recipesManager, cookBe);
    }

    public int getOutputMealSlot() {
        return outputMealSlot;
    }

    public int getOutputContainerSlot() {
        return outputContainerSlot;
    }

    @Override
    public ItemStack getOutputContainerStack() {
        return this.getStackInSlot(outputContainerSlot);
    }

    @Override
    public ItemStack getOutputMealStack() {
        return this.getStackInSlot(outputMealSlot);
    }
}
