package com.github.wallev.maidsoulkitchen.mixin.compat.kitchkarrot;

import com.github.wallev.maidsoulkitchen.api.mixin.IMaidsoulKitchenInterface;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixin;
import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@TaskMixin(task = TaskInfo.KK_AIR_COMPRESSOR)
@Mixin(value = AirCompressorBlockEntity.class, remap = false)
public interface AirCompressorBlockEntityAccessor extends IMaidsoulKitchenInterface {
    @Invoker("getRecipeFromItems")
    AirCompressorRecipe mk$getRecipeFromItems();

    @Invoker("isStarted")
    boolean mk$isStarted();

    @Invoker("hasEnergy")
    boolean mk$hasEnergy();
}
