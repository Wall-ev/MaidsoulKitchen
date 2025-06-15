package com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.beerbarrel;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.mixin.drinkbeer.BeerBarrelBlockAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class BeerBarrelBe extends CookBeBase<BeerBarrelBlockEntity> {
    public BeerBarrelBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof BeerBarrelBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be.getBrewingInventory();
    }

    @Override
    public int getIngredientSize() {
        return 5;
    }

    @Override
    public int getResultSlot() {
        return 5;
    }

    @Override
    public boolean recMatch() {
        if (((BeerBarrelBlockAccessor) be).tlmk$statusCode() == 1) {
            return true;
        }

        Optional<BrewingRecipe> recipe = serverLevel.getRecipeManager().getRecipeFor(RecipeRegistry.RECIPE_TYPE_BREWING.get(), be.getBrewingInventory(), serverLevel);
        return recipe.filter(brewingRecipe -> ((BeerBarrelBlockAccessor) be).tlmk$canBrew(brewingRecipe) && ((BeerBarrelBlockAccessor) be).tlmk$hasEnoughEmptyCap(brewingRecipe)).isPresent();
    }

    @Override
    public boolean cookStateMatch() {
        return true;
    }

    @Override
    public boolean canTakeResult() {
        return ((BeerBarrelBlockAccessor)be).tlmk$statusCode() == 2;
    }

    @Override
    public void markChanged() {
        be.markDirty();
    }
}
