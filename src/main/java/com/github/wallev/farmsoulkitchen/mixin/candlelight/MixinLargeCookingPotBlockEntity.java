package com.github.wallev.farmsoulkitchen.mixin.candlelight;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.candlelight.entity.LargeCookingPotBlockEntity;
import net.satisfy.farm_and_charm.recipe.CookingPotRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LargeCookingPotBlockEntity.class, remap = false)
public abstract class MixinLargeCookingPotBlockEntity implements ILdCbeAccessor<LargeCookingPotBlockEntity, CookingPotRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(CookingPotRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
