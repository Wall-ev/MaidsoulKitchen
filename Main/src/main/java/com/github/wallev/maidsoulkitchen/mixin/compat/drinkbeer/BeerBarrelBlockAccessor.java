package com.github.wallev.maidsoulkitchen.mixin.compat.drinkbeer;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@TaskMixin(value = {TaskInfo.DB_BEER, TaskInfo.MSM_DB_DRINKBEER_BEERBARREL})
@Mixin(value = BeerBarrelBlockEntity.class, remap = false)
public interface BeerBarrelBlockAccessor extends IMskMixinInterface {

    @Accessor("statusCode")
    int tlmk$statusCode();

    @Invoker("canBrew")
    boolean tlmk$canBrew(@Nullable BrewingRecipe recipe);

    @Invoker("hasEnoughEmptyCap")
    boolean tlmk$hasEnoughEmptyCap(BrewingRecipe recipe);
}
