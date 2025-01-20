package com.github.wallev.maidsoulkitchen.handler.initializer.farmersdelight.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.OutputContainerIMbe;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public class FdCookingPotContainer extends OutputContainerIMbe<CookingPotBlockEntity, CookingPotRecipe> {
    public FdCookingPotContainer(EntityMaid maid, MaidRecipesManager<?, CookingPotBlockEntity, CookingPotRecipe> recipesManager) {
        super(maid, recipesManager);
    }

    @Override
    protected void initialSlots() {
        this.inputSlotSize = 6;
        this.outputSlot = CookingPotBlockEntity.OUTPUT_SLOT;
        this.outputContainerSlot = CookingPotBlockEntity.CONTAINER_SLOT;
        this.outputMealSlot = CookingPotBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public ItemStackHandler getCookBeInv() {
        return this.cookBe.getInventory();
    }

    @Override
    public ItemStack getMealContainerItem() {
        return this.cookBe.getContainer();
    }
}
