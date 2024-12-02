package com.github.wallev.farmsoulkitchen.mixin.vinery;

import com.github.wallev.farmsoulkitchen.task.cook.v1.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.satisfy.vinery.block.entity.FermentationBarrelBlockEntity;
import net.satisfy.vinery.recipe.FermentationBarrelRecipe;

@Mixin(value = FermentationBarrelBlockEntity.class, remap = false)
public abstract class MixinFermentationBarrelBlockEntity implements ILdCbeAccessor<FermentationBarrelBlockEntity, FermentationBarrelRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(FermentationBarrelRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
