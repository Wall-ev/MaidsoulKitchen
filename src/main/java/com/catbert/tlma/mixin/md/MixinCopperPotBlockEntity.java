package com.catbert.tlma.mixin.md;

import com.catbert.tlma.api.CbeAccessor;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;

@Mixin(value = CopperPotBlockEntity.class, remap = false)
public abstract class MixinCopperPotBlockEntity implements CbeAccessor<CookingPotRecipe> {

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
