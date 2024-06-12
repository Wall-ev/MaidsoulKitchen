package com.catbert.tlma.mixin.fd;

import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public interface CookingPotBlockEntityAccessor {

    @Invoker("getMatchingRecipe")
    Optional<CookingPotRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper);

    @Invoker("canCook")
    boolean canCook$tlma(CookingPotRecipe recipe);

}
