package com.github.wallev.maidsoulkitchen.mixin.compat.kitchkarrot;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@TaskMixin(value = TaskInfo.KK_AIR_COMPRESSOR)
@Mixin(value = AirCompressorBlockEntity.class, remap = false)
public interface AirCompressorBlockEntityAccessor extends IMskMixinInterface {
    @Invoker("getRecipeFromItems")
    AirCompressorRecipe mk$getRecipeFromItems();

    @Invoker("isStarted")
    boolean mk$isStarted();

    @Invoker("hasEnergy")
    boolean mk$hasEnergy();
}
