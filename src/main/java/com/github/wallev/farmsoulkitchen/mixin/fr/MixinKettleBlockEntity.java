package com.github.wallev.farmsoulkitchen.mixin.fr;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettleRecipe;

import java.util.Optional;

@Mixin(value = KettleBlockEntity.class, remap = false)
public abstract class MixinKettleBlockEntity implements IFdCbeAccessor<KettleRecipe> {
    @Shadow protected abstract Optional<KettleRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow protected abstract boolean canBrew(KettleRecipe recipe, KettleBlockEntity kettle);

    @Override
    public Optional<KettleRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper) {
        return getMatchingRecipe(inventoryWrapper);
    }

    @Override
    public boolean canCook$tlma(KettleRecipe recipe) {
        return canBrew(recipe, ((KettleBlockEntity) (Object) this));
    }
}
