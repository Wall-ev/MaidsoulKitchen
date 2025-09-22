package com.github.wallev.maidsoulkitchen.compat.msm.kitchenkarrot.platecutting;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click.ICutterGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import io.github.tt432.kitchenkarrot.recipes.recipe.PlateRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;


@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_KK_PLATEC_CUTTING)
public class GeneratorKkPlateCuttingGuide implements ICutterGuideGenerator<PlateRecipe> {

    @Override
    public void generateSteps(BlockPos pos, Level level, PlateRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        craftGuide.addItemUse(realItems);

        int cutCount = this.cutCount(recipe);
        ItemStack result = outputs.get(0);
        ItemStack tool = containers.get(0);
        for (int i = 0; i < cutCount; i++) {
            craftGuide.addStep(new CraftGuideStepData(
                    TargetUtil.makeTargetVirtualNoSide(pos),
                    List.of(tool),
                    List.of(tool, result.copyWithCount(1)),
                    EnchantCommonUseAction.TYPE
            ));
        }
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<Ingredient> getInputs(PlateRecipe recipe) {
        return List.of(Ingredient.of(recipe.getInput()));
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModBlocks.PLATE.get().asItem();
    }

    @Override
    public List<Ingredient> getTools(PlateRecipe recipe) {
        return List.of(recipe.getTool());
    }

    @Override
    public int cutCount(PlateRecipe recipe) {
        return recipe.getResultCount() + 1;
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<PlateRecipe> getRecipeType() {
        return RecipeTypes.PLATE.get();
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.PLATE.get());
    }
}
