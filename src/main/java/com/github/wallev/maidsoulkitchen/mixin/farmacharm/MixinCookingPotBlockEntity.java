package com.github.wallev.maidsoulkitchen.mixin.farmacharm;

import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.farm_and_charm.block.entity.CookingPotBlockEntity;
import net.satisfy.farm_and_charm.recipe.CookingPotRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class MixinCookingPotBlockEntity implements ILdCbeAccessor<CookingPotBlockEntity, CookingPotRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(CookingPotRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
