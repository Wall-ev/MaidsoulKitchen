package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.water;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.init.ModRecipes;
import com.github.wallev.maidsoulkitchen.recipe.water.ConsumeWaterRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class BaseGetterWaterGuideGenerator implements ICookingRecipeGuideGenerator<ConsumeWaterRecipe> {
//public abstract class BaseGetterWaterGuideGenerator implements ICookingRecipeGuideGenerator<ConsumeWaterRecipe, Container> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, ConsumeWaterRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        craftGuide.addItemUse(realItems.get(0), outputs.get(0));
    }

    @Override
    public int getRecipeTime(ConsumeWaterRecipe recipe) {
        return 0;
    }

    @Override
    public RecipeType<ConsumeWaterRecipe> getRecipeType() {
        return ModRecipes.CONSUME_WATER_RECIPE;
    }
}
