package com.github.wallev.maidsoulkitchen.mixin.compat.drinkbeer;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.BeerBarrelBlockAccessor;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@TaskMixin(TaskInfo.DB_BEER)
@Mixin(value = BeerBarrelBlockEntity.class)
public abstract class BeerBarrelBlockMixin implements BeerBarrelBlockAccessor {
    @Shadow
    private int statusCode;

    @Shadow
    protected abstract boolean canBrew(@Nullable BrewingRecipe recipe);

    @Shadow
    protected abstract boolean hasEnoughEmptyCap(BrewingRecipe recipe);

    @Override
    public int tlmk$statusCode() {
        return statusCode;
    }

    @Override
    public boolean tlmk$canBrew(@Nullable BrewingRecipe recipe) {
        return canBrew(recipe);
    }

    @Override
    public boolean tlmk$hasEnoughEmptyCap(BrewingRecipe recipe) {
        return hasEnoughEmptyCap(recipe);
    }
}
