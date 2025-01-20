package com.github.wallev.maidsoulkitchen.handler.initializer.brewinandchewin.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.OutputContainerIMbe;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;

public class BncKeyContainer extends OutputContainerIMbe<KegBlockEntity, KegRecipe> {
    public BncKeyContainer(EntityMaid maid, MaidRecipesManager<?, KegBlockEntity, KegRecipe> recipesManager) {
        super(maid, recipesManager);
    }

    /**
     * 初始化厨具的格子信息（比如原料输入的格子，起始格子，输出的格子）
     */
    @Override
    protected void initialSlots() {
        this.inputSlotSize = 5;
        this.outputSlot = KegBlockEntity.OUTPUT_SLOT;
        this.outputContainerSlot = KegBlockEntity.CONTAINER_SLOT;
        this.outputMealSlot = KegBlockEntity.DRINK_DISPLAY_SLOT;
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
