package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class OutputContainerCMbe<B extends BlockEntity, R extends Recipe<? extends Container>> extends ContainerMaidCookBe<B, R> implements IOutputAddition.NeedOutputContainer {
    // @final
    private int outputContainerSlot;
    // @final
    private int outputMealSlot;
    public OutputContainerCMbe(EntityMaid maid, MaidRecipesManager<?, B, R> recipesManager) {
        super(maid, recipesManager);
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
