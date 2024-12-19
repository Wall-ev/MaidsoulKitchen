package com.github.wallev.maidsoulkitchen.mixin.candlelight;

import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.candlelight.entity.CookingPanBlockEntity;
import net.satisfy.farm_and_charm.recipe.RoasterRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = CookingPanBlockEntity.class, remap = false)
public abstract class MixinCookingPanBlockEntity implements ILdCbeAccessor<CookingPanBlockEntity, RoasterRecipe> {

    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(RoasterRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
