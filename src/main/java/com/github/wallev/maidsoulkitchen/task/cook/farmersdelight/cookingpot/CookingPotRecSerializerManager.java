package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cookingpot;

import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CookingPotRecSerializerManager extends RecSerializerManager<CookingPotRecipe> {
    private static final CookingPotRecSerializerManager INSTANCE = new CookingPotRecSerializerManager();

    protected CookingPotRecSerializerManager() {
        super(ModRecipeTypes.COOKING.get());
    }

    public static CookingPotRecSerializerManager getInstance() {
        return INSTANCE;
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
