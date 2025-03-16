package com.github.wallev.maidsoulkitchen.mixin.brewinandchewin;

import com.brewinandchewin.common.block.entity.KegBlockEntity;
import com.brewinandchewin.common.crafting.KegRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(value = KegBlockEntity.class, remap = false)
public interface KegBlockEntityAccessor {

    @Invoker("getMatchingRecipe")
    Optional<KegRecipe> tlmk$getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Invoker("canFerment")
    boolean tlmk$canCook(KegRecipe recipe);

}
