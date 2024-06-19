package com.github.catbert.tlma.mixin.candlelight;

import com.github.catbert.tlma.api.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.candlelight.block.entity.CookingPanBlockEntity;
import satisfy.candlelight.recipe.CookingPanRecipe;

@Mixin(value = CookingPanBlockEntity.class, remap = false)
public abstract class MixinCookingPanBlockEntity implements ILdCbeAccessor<CookingPanBlockEntity, CookingPanRecipe> {

    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(CookingPanRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
