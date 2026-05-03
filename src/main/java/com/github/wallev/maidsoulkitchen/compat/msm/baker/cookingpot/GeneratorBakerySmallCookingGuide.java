package com.github.wallev.maidsoulkitchen.compat.msm.baker.cookingpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
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
import net.satisfy.bakery.core.block.entity.SmallCookingPotBlockEntity;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_DO_BEKERY_SMALL_COOKING_POT)
public class GeneratorBakerySmallCookingGuide implements ILdCookingPotGuideGenerator<CookingPotRecipe, SmallCookingPotBlockEntity> {
//public class GeneratorBakerySmallCookingGuide implements ILdCookingPotGuideGenerator<CookingPotRecipe, Container, SmallCookingPotBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof SmallCookingPotBlockEntity;
    }

    @Override
    public boolean isHeated(SmallCookingPotBlockEntity be) {
        return be.isBeingBurned();
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return RecipeTypeRegistry.COOKING_POT_RECIPE_TYPE.get();
    }


    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("bakery", "pot_cooking");
    }

    @Override
    public int getRecipeTime(CookingPotRecipe recipe) {
        return 300;
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<Ingredient> getContainers(CookingPotRecipe recipe) {
        ItemStack containerItem = recipe.getContainerItem();
        if (!containerItem.isEmpty()) {
            return List.of(Ingredient.of(containerItem));
        } else {
            return List.of();
        }
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.SMALL_COOKING_POT_ITEM.get();
    }
}
