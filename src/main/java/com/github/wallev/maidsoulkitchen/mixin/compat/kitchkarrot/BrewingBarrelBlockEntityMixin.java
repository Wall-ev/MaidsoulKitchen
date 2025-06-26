package com.github.wallev.maidsoulkitchen.mixin.compat.kitchkarrot;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixin;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.BrewingBarrelRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@TaskMixin(task = TaskInfo.KK_BREW_BARREL)
@Mixin(value = BrewingBarrelBlockEntity.class, remap = false)
public abstract class BrewingBarrelBlockEntityMixin implements ICookBeAccessor {
//    @Shadow
//    protected abstract boolean hasRecipe();

    @Shadow public abstract Optional<BrewingBarrelRecipe> findRecipe();

    @Override
    public boolean kl$canCook() {
        return this.findRecipe().isPresent();
//         @todo fix
//        return false;
//        return this.hasRecipe();
    }
}
