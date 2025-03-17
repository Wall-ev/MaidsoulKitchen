package com.github.wallev.maidsoulkitchen.mixin.herbalbrews;

import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.herbalbrews.blocks.entity.CauldronBlockEntity;
import satisfy.herbalbrews.recipe.CauldronRecipe;

@Mixin(value = CauldronBlockEntity.class, remap = false)
public abstract class CauldronBlockEntityMixin implements ILdCbeAccessor<CauldronBlockEntity, CauldronRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean tlmk$canCraft(CauldronRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
