package com.github.catbert.tlma.mixin.herbal;

import com.github.catbert.tlma.api.ILdCbeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import satisfy.herbalbrews.blocks.entity.CauldronBlockEntity;
import satisfy.herbalbrews.recipe.CauldronRecipe;

@Mixin(value = CauldronBlockEntity.class, remap = false)
public abstract class MixinCauldronBlockEntity implements ILdCbeAccessor<CauldronBlockEntity, CauldronRecipe> {
    @Shadow protected abstract boolean canCraft(Recipe<?> recipe, RegistryAccess access);

    @Override
    public boolean canCraft$tlma(CauldronRecipe rec, RegistryAccess access) {
        return canCraft(rec, access);
    }
}
