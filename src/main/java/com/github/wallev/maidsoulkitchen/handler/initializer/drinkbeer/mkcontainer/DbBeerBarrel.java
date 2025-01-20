package com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.ContainerMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import net.minecraft.world.Container;

public class DbBeerBarrel extends ContainerMaidCookBe<BeerBarrelBlockEntity, BrewingRecipe> {
    public DbBeerBarrel(EntityMaid maid, MaidRecipesManager<?, BeerBarrelBlockEntity, BrewingRecipe> recipesManager) {
        super(maid, recipesManager);
    }

    /**
     * 初始化厨具的格子信息（比如原料输入的格子，起始格子，输出的格子）
     */
    @Override
    protected void initialSlots() {
        this.inputSlotSize = 5;
        this.outputSlot = 5;
    }

    @Override
    public Container getCookBeInv() {
        return this.cookBe.getBrewingInventory();
    }
}
