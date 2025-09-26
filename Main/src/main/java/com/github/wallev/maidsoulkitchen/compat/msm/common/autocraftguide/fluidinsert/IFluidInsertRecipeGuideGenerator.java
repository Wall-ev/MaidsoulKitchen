package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.fluidinsert;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IFluidInsertRecipeGuideGenerator<R extends Recipe<? extends Container>> extends ICookingRecipeGuideGenerator<R> {

    @Override
    default void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        this.addFluidStep(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
        ICookingRecipeGuideGenerator.super.generateSteps(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
    }

    default void addFluidStep(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ItemStack fluid = realItems.remove(0);
        craftGuide.addItemUse(fluid);
    }

    @Override
    default List<Ingredient> getInputs(R recipe) {
        List<Ingredient> allInputs = Lists.newArrayList(getFluids(recipe));
        List<Ingredient> noFluidInputs = ICookingRecipeGuideGenerator.super.getInputs(recipe);
        allInputs.addAll(noFluidInputs);
        return allInputs;
    }

    List<Ingredient> getFluids(R recipe);

}
