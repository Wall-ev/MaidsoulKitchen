package com.github.wallev.farmsoulkitchen.mixin.fd;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class MixinCookingPotBlockEntity implements IFdCbeAccessor<CookingPotRecipe> {
    @Shadow
    protected abstract Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    protected abstract boolean canCook(CookingPotRecipe recipe);

    @Override
    public Optional<CookingPotRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper) {
        return getMatchingRecipe(inventoryWrapper);
    }

    @Override
    public boolean canCook$tlma(CookingPotRecipe recipe) {
        return canCook(recipe);
    }
}
