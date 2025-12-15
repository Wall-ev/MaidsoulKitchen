package com.github.wallev.maidsoulkitchen.compat.msm.copperpot.cooking;

import com.davigj.copperpot.common.block.entity.CopperPotBlockEntity;
import com.davigj.copperpot.core.registry.CPItems;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

@GuideTest
//@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_CPD_COPPER_POT)
public class GeneratorCopperPotCookingGuide implements IFdCookingPotGuideGenerator<CookingPotRecipe, CopperPotBlockEntity> {
//public class GeneratorCopperPotCookingGuide implements IFdCookingPotGuideGenerator<CookingPotRecipe, RecipeWrapper, CopperPotBlockEntity> {

    @Override
    public boolean isValidRecipe(CookingPotRecipe recipe) {
        return recipe.getIngredients().size() == 3;
    }

    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof CopperPotBlockEntity;
    }

    @Override
    public boolean isHeated(CopperPotBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("copperpot:cooking");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public List<Ingredient> getContainers(CookingPotRecipe recipe) {
        return toIngredients(recipe.getOutputContainer());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return CPItems.COPPER_POT.get();
    }

    @Override
    public int getRecipeTime(CookingPotRecipe recipe) {
        return recipe.getCookTime();
    }
}
