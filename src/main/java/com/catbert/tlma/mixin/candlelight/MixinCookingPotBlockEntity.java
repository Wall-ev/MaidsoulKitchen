package com.catbert.tlma.mixin.candlelight;

import com.catbert.tlma.api.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.candlelight.block.entity.CookingPotBlockEntity;
import satisfy.candlelight.recipe.CookingPotRecipe;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class MixinCookingPotBlockEntity implements ILdCbeAccessor<CookingPotBlockEntity, CookingPotRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(CookingPotRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
