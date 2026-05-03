package com.github.wallev.maidsoulkitchen.compat.msm.kitchenkarrot.aircompressor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.google.common.collect.Lists;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_KK_AIR_COMPRESSOR)
public class GeneratorKkAirCompressorGuide implements ICookingRecipeGuideGenerator<AirCompressorRecipe> {

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<Ingredient> getInputs(AirCompressorRecipe recipe) {
        ArrayList<Ingredient> allInputs = Lists.newArrayList(recipe.getIngredient());
        Ingredient container = recipe.getContainer();
        if (container != null && !container.isEmpty()) {
            allInputs.add(container);
        }
        return allInputs;
    }

    @Override
    public List<Ingredient> getContainers(AirCompressorRecipe recipe) {
        return recipe.getContainer() == null ? List.of() : Collections.singletonList(recipe.getContainer());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModBlocks.AIR_COMPRESSOR.get().asItem();
    }

    @Override
    public int getRecipeTime(AirCompressorRecipe recipe) {
        return recipe.getCraftingTime();
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<AirCompressorRecipe> getRecipeType() {
        return RecipeTypes.AIR_COMPRESSOR.get();
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.AIR_COMPRESSOR.get());
    }

    @Override
    public void remainStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> remains) {
    }
}
