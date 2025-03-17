package com.github.wallev.maidsoulkitchen.mixin.herbalbrews;

import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.herbalbrews.blocks.entity.TeaKettleBlockEntity;
import satisfy.herbalbrews.recipe.TeaKettleRecipe;

@Mixin(value = TeaKettleBlockEntity.class, remap = false)
public abstract class TeaKettleBlockEntityMixin implements ILdCbeAccessor<TeaKettleBlockEntity, TeaKettleRecipe> {
    @Shadow protected abstract boolean canCraft(TeaKettleRecipe recipe);

    @Override
    public boolean tlmk$canCraft(TeaKettleRecipe rec, RegistryAccess access) {
        return canCraft(rec);
    }
}
