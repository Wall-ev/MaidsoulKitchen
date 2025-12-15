package com.github.wallev.maidsoulkitchen.compat.msm.vintagedelight.fermentingjar;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.mixin.compat.vintagedelight.FermentingRecipeAccessor;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.ribs.vintagedelight.block.ModBlocks;
import net.ribs.vintagedelight.block.entity.FermentingJarBlockEntity;
import net.ribs.vintagedelight.item.ModItems;
import net.ribs.vintagedelight.recipe.FermentingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_VTD_FERMENTING_JAR)
public class GeneratorVdFermentingJarGuide implements IFdCookingPotGuideGenerator<FermentingRecipe, FermentingJarBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof FermentingJarBlockEntity;
    }

    @Override
    public boolean isHeated(FermentingJarBlockEntity be) {
        return true;
    }

    @Override
    public RecipeType<FermentingRecipe> getRecipeType() {
        return FermentingRecipe.Type.INSTANCE;
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(FermentingRecipe.Serializer.ID);
    }

    @Override
    public List<Ingredient> getContainers(FermentingRecipe recipe) {
        FermentingRecipeAccessor accessor = (FermentingRecipeAccessor) recipe;
        Ingredient containerIngredient = accessor.msk$getContainerIngredient();
        return List.of(containerIngredient);
    }

    @Override
    public List<ItemStack> getOutputs(FermentingRecipe recipe, RegistryAccess registryAccess) {
        ItemStack resultItem = recipe.getResultItem(RegistryAccess.EMPTY);
        ItemStack secondaryResultItem = recipe.getSecondaryResultItem();
        return List.of(resultItem, secondaryResultItem);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModBlocks.FERMENTING_JAR.get().asItem();
    }

    @Override
    public int getRecipeTime(FermentingRecipe recipe) {
        return recipe.getProcessingTime();
    }
}
