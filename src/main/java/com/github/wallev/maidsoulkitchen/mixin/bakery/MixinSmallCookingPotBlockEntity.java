package com.github.wallev.maidsoulkitchen.mixin.bakery;

import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.bakery.block.entity.SmallCookingPotBlockEntity;
import net.satisfy.bakery.recipe.CookingPotRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SmallCookingPotBlockEntity.class, remap = false)
public abstract class MixinSmallCookingPotBlockEntity implements ILdCbeAccessor<SmallCookingPotBlockEntity, CookingPotRecipe> {
    @Shadow
    protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(CookingPotRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
