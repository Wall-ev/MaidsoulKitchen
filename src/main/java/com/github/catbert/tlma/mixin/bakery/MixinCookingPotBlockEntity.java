package com.github.catbert.tlma.mixin.bakery;

import com.github.catbert.tlma.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.satisfy.bakery.block.entity.CookingPotBlockEntity;
import net.satisfy.bakery.recipe.CookingPotRecipe;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class MixinCookingPotBlockEntity implements ILdCbeAccessor<CookingPotBlockEntity, CookingPotRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(CookingPotRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
