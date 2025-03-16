package com.github.wallev.maidsoulkitchen.mixin.brewinandchewin;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;

import java.util.Optional;

@Mixin(value = KegBlockEntity.class, remap = false)
public interface KegBlockEntityAccessor {

    @Invoker("getMatchingRecipe")
    Optional<KegRecipe> tlmk$getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Invoker("canFerment")
    boolean tlmk$canCook(KegRecipe recipe);

}
