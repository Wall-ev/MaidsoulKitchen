package com.github.wallev.farmsoulkitchen.mixin.farmacharm;

import com.github.wallev.farmsoulkitchen.task.cook.v1.bakery.IStoveBe;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.bestate.IFuelBe;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.satisfy.farm_and_charm.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.recipe.StoveRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = StoveBlockEntity.class, remap = false)
public abstract class MixinStoveBlockEntity implements ILdCbeAccessor<StoveBlockEntity, StoveRecipe>, IFuelBe, IStoveBe {

    @Shadow protected abstract boolean isBurning();

    @Shadow protected abstract boolean canCraft(StoveRecipe recipe, RegistryAccess access);

    @Shadow protected abstract int getTotalBurnTime(ItemStack fuel);

    @Override
    public boolean canCraft$tlma(StoveRecipe rec, RegistryAccess access) {
        return isBurning() && canCraft(rec, access);
    }

    @Override
    public boolean isBurning$tlma() {
        return isBurning();
    }

    @Override
    public int getTotalBurnTime$tlma(ItemStack fuel) {
        return getTotalBurnTime(fuel);
    }
}
