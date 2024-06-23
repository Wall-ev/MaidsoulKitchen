package com.github.catbert.tlma.mixin.bakery;

import com.github.catbert.tlma.api.ILdCbeAccessor;
import com.github.catbert.tlma.api.task.v1.bestate.IFuelBe;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.bakery.block.entity.StoveBlockEntity;
import satisfy.bakery.recipe.StoveRecipe;

@Mixin(value = StoveBlockEntity.class, remap = false)
public abstract class MixinStoveBlockEntity implements ILdCbeAccessor<StoveBlockEntity, StoveRecipe>, IFuelBe {

    @Shadow protected abstract boolean isBurning();

    @Shadow protected abstract boolean canCraft(StoveRecipe recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(StoveRecipe rec, RegistryAccess access) {
        return isBurning() && canCraft(rec, access);
    }

    @Override
    public boolean isBurning$tlma() {
        return isBurning();
    }
}
