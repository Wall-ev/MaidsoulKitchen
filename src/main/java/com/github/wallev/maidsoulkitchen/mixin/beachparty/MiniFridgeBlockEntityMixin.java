package com.github.wallev.maidsoulkitchen.mixin.beachparty;

import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.beachparty.block.entity.MiniFridgeBlockEntity;
import satisfy.beachparty.recipe.MiniFridgeRecipe;

@Mixin(value = MiniFridgeBlockEntity.class, remap = false)
public abstract class MiniFridgeBlockEntityMixin implements ILdCbeAccessor<MiniFridgeBlockEntity, MiniFridgeRecipe> {

    @Shadow
    protected abstract boolean canCraft(MiniFridgeRecipe recipe, RegistryAccess access);

    @Override
    public boolean tlmk$canCraft(MiniFridgeRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
