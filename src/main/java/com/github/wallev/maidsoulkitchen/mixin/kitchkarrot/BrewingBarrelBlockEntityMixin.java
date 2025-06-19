package com.github.wallev.maidsoulkitchen.mixin.kitchkarrot;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BrewingBarrelBlockEntity.class, remap = false)
public abstract class BrewingBarrelBlockEntityMixin implements ICookBeAccessor {
//    @Shadow protected abstract boolean hasRecipe();

    @Override
    public boolean kl$canCook() {
        // @todo fix
        return false;
//        return this.hasRecipe();
    }
}
