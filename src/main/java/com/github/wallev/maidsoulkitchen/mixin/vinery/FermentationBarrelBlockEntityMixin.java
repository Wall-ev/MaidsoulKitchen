package com.github.wallev.maidsoulkitchen.mixin.vinery;

import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.vinery.block.entity.FermentationBarrelBlockEntity;
import net.satisfy.vinery.recipe.FermentationBarrelRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = FermentationBarrelBlockEntity.class, remap = false)
public abstract class FermentationBarrelBlockEntityMixin implements ILdCbeAccessor<FermentationBarrelBlockEntity, FermentationBarrelRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean tlmk$canCraft(FermentationBarrelRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
