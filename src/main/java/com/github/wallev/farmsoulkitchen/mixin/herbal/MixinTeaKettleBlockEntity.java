package com.github.wallev.farmsoulkitchen.mixin.herbal;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.herbalbrews.blocks.entity.TeaKettleBlockEntity;
import satisfy.herbalbrews.recipe.TeaKettleRecipe;

@Mixin(value = TeaKettleBlockEntity.class, remap = false)
public abstract class MixinTeaKettleBlockEntity implements ILdCbeAccessor<TeaKettleBlockEntity, TeaKettleRecipe> {
    @Shadow protected abstract boolean canCraft(TeaKettleRecipe recipe);

    @Override
    public boolean canCraft$tlma(TeaKettleRecipe rec, RegistryAccess access) {
        return canCraft(rec);
    }
}
