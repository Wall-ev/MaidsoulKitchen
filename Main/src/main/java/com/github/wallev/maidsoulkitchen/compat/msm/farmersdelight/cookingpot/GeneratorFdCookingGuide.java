package com.github.wallev.maidsoulkitchen.compat.msm.farmersdelight.cookingpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FD_COOKING_POT)
public class GeneratorFdCookingGuide implements IFdCookingPotGuideGenerator<CookingPotRecipe, CookingPotBlockEntity> {
//public class GeneratorFdCookingGuide implements IFdCookingPotGuideGenerator<CookingPotRecipe, RecipeWrapper, CookingPotBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof CookingPotBlockEntity;
    }

    @Override
    public boolean isHeated(CookingPotBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public int getRecipeTime(CookingPotRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    public List<Ingredient> getContainers(CookingPotRecipe recipe) {
        ItemStack output = recipe.getResultItem(RegistryAccess.EMPTY);

        ItemStack outputContainer = recipe.getOutputContainer();
        boolean empty = outputContainer.isEmpty();
        if (empty) {
            return List.of();
        } else {
            outputContainer.setCount(output.getCount());
            return toIngredients(outputContainer);
        }
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.COOKING_POT.get();
    }
}
