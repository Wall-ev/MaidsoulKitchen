package com.github.wallev.maidsoulkitchen.compat.msm.farmersdelight.cutting;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click.ICutterGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FD_CUTTING_BOARD)
public class GeneratorFdCuttingGuide implements ICutterGuideGenerator<CuttingBoardRecipe> {
//public class GeneratorFdCuttingGuide implements ICutterGuideGenerator<CuttingBoardRecipe, RecipeWrapper> {
    @Override
    public List<Ingredient> getTools(CuttingBoardRecipe recipe) {
        return List.of(recipe.getTool());
    }

    @Override
    public int cutCount(CuttingBoardRecipe recipe) {
        return 1;
    }

    /**
     * 检查位置是否合法。用于判断和世界相关的成立条件。
     *
     * @param level       世界
     * @param maid        女仆
     * @param pos         位置
     * @param pathFinding 路径查找
     * @return 是否合法
     */
    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.CUTTING_BOARD.get();
    }

    @Override
    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return ModRecipeTypes.CUTTING.get();
    }

    /**
     * 方块是否合法。用于判断和世界无关的成立条件
     *
     * @param level 世界
     * @param pos   方块位置
     * @return 是否合法
     */
    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.CUTTING_BOARD.get());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }
}
