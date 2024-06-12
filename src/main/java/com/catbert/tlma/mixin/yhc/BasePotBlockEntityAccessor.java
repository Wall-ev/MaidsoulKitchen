package com.catbert.tlma.mixin.yhc;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(value = BasePotBlockEntity.class, remap = false)
public interface BasePotBlockEntityAccessor {

    @Invoker("getMatchingRecipe")
    Optional<? extends BasePotRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper);

    @Invoker("canCook")
    boolean canCook$tlma(BasePotRecipe recipe);

}
