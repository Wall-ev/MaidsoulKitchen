package com.github.wallev.farmsoulkitchen.mixin.mc;

import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceAccessor {

    @Final
    @Accessor(remap = false)
    RecipeType<? extends AbstractCookingRecipe> getRecipeType();

    @Invoker("isLit")
    boolean isBurning$tlma();
}
