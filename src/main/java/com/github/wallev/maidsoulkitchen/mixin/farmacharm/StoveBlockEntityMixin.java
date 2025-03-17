package com.github.wallev.maidsoulkitchen.mixin.farmacharm;

import com.github.wallev.maidsoulkitchen.task.cook.bakery.IStoveBe;
import com.github.wallev.maidsoulkitchen.task.cook.common.bestate.IFuelBe;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.satisfy.farm_and_charm.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.recipe.StoveRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = StoveBlockEntity.class, remap = false)
public abstract class StoveBlockEntityMixin implements ILdCbeAccessor<StoveBlockEntity, StoveRecipe>, IFuelBe, IStoveBe {

    @Shadow protected abstract boolean isBurning();

    @Shadow protected abstract boolean canCraft(StoveRecipe recipe, RegistryAccess access);

    @Shadow protected abstract int getTotalBurnTime(ItemStack fuel);

    @Override
    public boolean tlmk$canCraft(StoveRecipe rec, RegistryAccess access) {
        return isBurning() && canCraft(rec, access);
    }

    @Override
    public boolean tlmk$isBurning() {
        return isBurning();
    }

    @Override
    public int tlmk$getTotalBurnTime(ItemStack fuel) {
        return getTotalBurnTime(fuel);
    }
}
