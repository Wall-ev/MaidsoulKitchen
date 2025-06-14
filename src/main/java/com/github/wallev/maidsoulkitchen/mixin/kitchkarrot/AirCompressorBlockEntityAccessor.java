package com.github.wallev.maidsoulkitchen.mixin.kitchkarrot;

import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = AirCompressorBlockEntity.class, remap = false)
public interface AirCompressorBlockEntityAccessor {
    @Invoker("getRecipeFromItems")
    AirCompressorRecipe mk$getRecipeFromItems();

    @Invoker("isStarted")
    boolean mk$isStarted();

    @Invoker("hasEnergy")
    boolean mk$hasEnergy();
}
