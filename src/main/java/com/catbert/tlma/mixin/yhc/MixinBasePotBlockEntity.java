package com.catbert.tlma.mixin.yhc;

import com.catbert.tlma.api.ICbeAccessor;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(value = BasePotBlockEntity.class, remap = false)
public abstract class MixinBasePotBlockEntity implements ICbeAccessor<BasePotRecipe> {

    @Shadow
    protected abstract Optional<? extends BasePotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    protected abstract boolean canCook(BasePotRecipe recipe);

    @Override
    @SuppressWarnings("unchecked")
    public Optional<BasePotRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper) {
        return (Optional<BasePotRecipe>) getMatchingRecipe(inventoryWrapper);
    }

    @Override
    public boolean canCook$tlma(BasePotRecipe recipe) {
        return canCook(recipe);
    }
}
