package com.github.wallev.maidsoulkitchenlegacy.mixin.kitchkarrot;

import com.github.wallev.maidsoulkitchen.legacy.mixin.LegacyTaskMixin;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@LegacyTaskMixin(mod = Mods.KK_LEGACY, task = TaskInfo.KK_BREW_BARREL_LEGACY)
@Mixin(value = BrewingBarrelBlockEntity.class, remap = false)
public abstract class BrewingBarrelBlockEntityMixin implements ICookBeAccessor {
    @Shadow
    protected abstract boolean hasRecipe();

    @Override
    public boolean kl$canCook() {
        return this.hasRecipe();
    }
}
