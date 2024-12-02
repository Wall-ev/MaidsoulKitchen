package com.github.wallev.farmsoulkitchen.mixin.beachparty;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.beachparty.block.entity.MiniFridgeBlockEntity;
import satisfy.beachparty.recipe.MiniFridgeRecipe;

@Mixin(value = MiniFridgeBlockEntity.class, remap = false)
public abstract class MixinMiniFridgeBlockEntity implements ILdCbeAccessor<MiniFridgeBlockEntity, MiniFridgeRecipe> {

    @Shadow
    protected abstract boolean canCraft(MiniFridgeRecipe recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(MiniFridgeRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
