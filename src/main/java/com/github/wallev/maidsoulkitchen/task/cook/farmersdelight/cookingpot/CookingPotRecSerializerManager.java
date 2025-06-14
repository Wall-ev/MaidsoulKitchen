package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cookingpot;

import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CookingPotRecSerializerManager extends RecSerializerManager<CookingPotRecipe> {
    public CookingPotRecSerializerManager() {
        super(ModRecipeTypes.COOKING.get());
    }

    @Override
    protected RecipeInfoProvider<CookingPotRecipe> createRecipeInfoProvider() {
        return new CookingPotRecipeInfo();
    }

    public static class CookingPotRecipeInfo extends RecipeInfoProvider<CookingPotRecipe> {

        @Override
        public ItemStack getContainer(RecSerializerManager<CookingPotRecipe> rsm, CookingPotRecipe rec) {
            return rec.getOutputContainer();
        }
    }
}
