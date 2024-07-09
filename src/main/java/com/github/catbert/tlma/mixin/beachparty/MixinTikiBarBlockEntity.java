package com.github.catbert.tlma.mixin.beachparty;

import com.github.catbert.tlma.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.beachparty.block.entity.TikiBarBlockEntity;
import satisfy.beachparty.recipe.TikiBarRecipe;

@Mixin(value = TikiBarBlockEntity.class, remap = false)
public abstract class MixinTikiBarBlockEntity implements ILdCbeAccessor<TikiBarBlockEntity, TikiBarRecipe> {

    @Shadow
    protected abstract boolean canCraft(TikiBarRecipe recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(TikiBarRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
