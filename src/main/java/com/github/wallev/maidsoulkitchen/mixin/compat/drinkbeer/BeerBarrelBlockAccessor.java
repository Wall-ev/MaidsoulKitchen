package com.github.wallev.maidsoulkitchen.mixin.compat.drinkbeer;

import com.github.wallev.maidsoulkitchen.api.mixin.IMaidsoulKitchenInterface;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(value = BeerBarrelBlockEntity.class, remap = false)
public interface BeerBarrelBlockAccessor extends IMaidsoulKitchenInterface {

    @Accessor("statusCode")
    int tlmk$statusCode();

    @Invoker("canBrew")
    boolean tlmk$canBrew(@Nullable BrewingRecipe recipe);

    @Invoker("hasEnoughEmptyCap")
    boolean tlmk$hasEnoughEmptyCap(BrewingRecipe recipe);
}
