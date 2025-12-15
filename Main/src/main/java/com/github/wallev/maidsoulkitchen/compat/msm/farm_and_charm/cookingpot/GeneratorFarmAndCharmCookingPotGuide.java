package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.cookingpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.farm_and_charm.core.block.entity.CookingPotBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FARM_AND_CHARM_COOKING_POT)
public class GeneratorFarmAndCharmCookingPotGuide implements ILdCookingPotGuideGenerator<CookingPotRecipe, CookingPotBlockEntity> {
//public class GeneratorFarmAndCharmCookingPotGuide implements ILdCookingPotGuideGenerator<CookingPotRecipe, Container, CookingPotBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof CookingPotBlockEntity;
    }

    @Override
    public boolean isHeated(CookingPotBlockEntity be) {
        return be.isBeingBurned();
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return RecipeTypeRegistry.COOKING_POT_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.ofTypeMod("farm_and_charm", "pot_cooking");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<Ingredient> getContainers(CookingPotRecipe recipe) {
        return toIngredients(recipe.getContainerItem());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.COOKING_POT.get().asItem();
    }

    @Override
    public int getRecipeTime(CookingPotRecipe recipe) {
        return 900;
    }
}
