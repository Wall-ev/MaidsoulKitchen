package com.github.wallev.maidsoulkitchen.compat.msm.kitchenkarrot.brewing;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.fluidinsert.IFluidInsertRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.google.common.collect.Lists;
import io.github.tt432.kitchenkarrot.recipes.recipe.BrewingBarrelRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_KK_BREWING)
public class GeneratorKkBrewingGuide implements IFluidInsertRecipeGuideGenerator<BrewingBarrelRecipe> {

    @Override
    public void generateSteps(BlockPos pos, Level level, BrewingBarrelRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        IFluidInsertRecipeGuideGenerator.super.generateSteps(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
    }

    @Override
    public List<Ingredient> getFluids(BrewingBarrelRecipe recipe) {
        return List.of(Ingredient.of(Items.WATER_BUCKET));
    }

    @Override
    public int getRecipeTime(BrewingBarrelRecipe recipe) {
        return recipe.getCraftingTime();
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<Ingredient> getInputs(BrewingBarrelRecipe recipe) {
        List<Ingredient> allInputs = Lists.newArrayList(getFluids(recipe));
        List<Ingredient> noFluidInputs = recipe.getIngredient();
        allInputs.addAll(noFluidInputs);
        return allInputs;
    }

    @Override
    public List<ItemStack> getOutputs(BrewingBarrelRecipe recipe, RegistryAccess registryAccess) {
        return IFluidInsertRecipeGuideGenerator.super.getOutputs(recipe, registryAccess);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModBlocks.BREWING_BARREL.get().asItem();
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<BrewingBarrelRecipe> getRecipeType() {
        return RecipeTypes.BREWING_BARREL.get();
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.BREWING_BARREL.get());
    }

    @Override
    public void outputContainerStep(BlockPos pos, boolean needContainer, CraftGuideOperator2 craftGuide, List<ItemStack> containers) {
    }

    @Override
    public void remainStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> remains) {
    }
}
