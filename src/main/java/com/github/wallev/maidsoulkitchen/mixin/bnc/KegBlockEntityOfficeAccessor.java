package com.github.wallev.maidsoulkitchen.mixin.bnc;

import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.utility.KegRecipeWrapper;

import java.util.Optional;

@Mixin(value = KegBlockEntity.class, remap = false)
public interface KegBlockEntityOfficeAccessor {

    @Invoker
    boolean callCanFerment(KegFermentingRecipe recipe, KegBlockEntity keg);

    @Invoker
    Optional<KegFermentingRecipe> callGetMatchingRecipe(KegRecipeWrapper inventoryWrapper);

    @Accessor
    KegRecipeWrapper getRecipeWrapper();
}
