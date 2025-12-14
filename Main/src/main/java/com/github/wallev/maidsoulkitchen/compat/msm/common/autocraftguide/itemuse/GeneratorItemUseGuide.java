package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.itemuse;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.init.ModRecipes;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.recipe.itemuse.ItemUseRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.ICachableGeneratorGraph;
import studio.fantasyit.maid_storage_manager.data.InventoryItem;

import java.util.List;
import java.util.Map;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_ITEM_USE)
public class GeneratorItemUseGuide implements ICookingRecipeGuideGenerator<ItemUseRecipe> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return isSameMaidPos(maid, pos);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return false;
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, ItemUseRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos.above(1)),
                realItems,
                outputs,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(EnchantCommonUseAction.OPTION_USE_METHOD, recipe.condition.toUseType())
        ));
    }

    @Override
    public int getRecipeTime(ItemUseRecipe recipe) {
        return 0;
    }

    @TypeLang(
            en_us = "Item Use",
            zh_cn = "物品右键使用"
    )
    @Override
    public RecipeType<ItemUseRecipe> getRecipeType() {
        return ModRecipes.ITEM_USE_RECIPE;
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<ItemStack> getOutputs(ItemUseRecipe recipe, RegistryAccess registryAccess) {
        return recipe.results;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return DEFAULT_ITEM;
    }

    @Override
    public void generate(List<InventoryItem> inventory, Level level, BlockPos pos, ICachableGeneratorGraph graph, Map<ResourceLocation, List<BlockPos>> recognizedTypePositions) {
        ICookingRecipeGuideGenerator.super.generate(inventory, level, pos, graph, recognizedTypePositions);
    }
}
