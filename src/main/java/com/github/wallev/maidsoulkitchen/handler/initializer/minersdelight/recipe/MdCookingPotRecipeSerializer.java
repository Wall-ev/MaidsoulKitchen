package com.github.wallev.maidsoulkitchen.handler.initializer.minersdelight.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.OutputContainerCookRecSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

public class MdCookingPotRecipeSerializer extends OutputContainerCookRecSerializer<CookingPotRecipe> {
    public MdCookingPotRecipeSerializer() {
        super(ModRecipeTypes.COOKING.get());
    }

    @Override
    public ItemStack getContainer(CookingPotRecipe recipe) {
        return recipe.getOutputContainer();
    }

    @Override
    protected List<CookingPotRecipe> getRecipes(Level level) {
        return super.getRecipes(level).stream().filter(cookingPotRecipe -> {
            return cookingPotRecipe.getIngredients().size() <= 4;
        }).toList();
    }
}
