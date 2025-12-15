package com.github.wallev.maidsoulkitchen.compat.msm.kaleidoscopecookery.choppingboard;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click.ICutterGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.ChoppingBoardRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_KC_CHOPPING_BOARD)
public class GeneratorKcChoppingBoardGuide implements ICutterGuideGenerator<ChoppingBoardRecipe> {
    @Override
    public List<Ingredient> getTools(ChoppingBoardRecipe recipe) {
        return List.of(Ingredient.of(TagMod.KITCHEN_KNIFE));
    }

    @Override
    public int cutCount(ChoppingBoardRecipe recipe) {
        return recipe.getCutCount() + 1;
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.CHOPPING_BOARD.get();
    }

    @Override
    public RecipeType<ChoppingBoardRecipe> getRecipeType() {
        return ModRecipes.CHOPPING_BOARD_RECIPE;
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.CHOPPING_BOARD.get());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, ChoppingBoardRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ICutterGuideGenerator.super.generateSteps(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
    }
}
