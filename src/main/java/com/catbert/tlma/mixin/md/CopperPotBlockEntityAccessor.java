package com.catbert.tlma.mixin.md;

import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;

@Mixin(value = CopperPotBlockEntity.class, remap = false)
public interface CopperPotBlockEntityAccessor {

    @Invoker("getMatchingRecipe")
    Optional<CookingPotRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper);

    @Invoker("canCook")
    boolean canCook$tlma(CookingPotRecipe recipe);
}
