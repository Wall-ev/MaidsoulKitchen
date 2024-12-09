package com.github.wallev.farmsoulkitchen.mixin.farmacharm;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.farm_and_charm.block.entity.RoasterBlockEntity;
import net.satisfy.farm_and_charm.recipe.RoasterRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = RoasterBlockEntity.class, remap = false)
public abstract class MixinRoastBlockEntity implements ILdCbeAccessor<RoasterBlockEntity, RoasterRecipe> {

    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(RoasterRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
