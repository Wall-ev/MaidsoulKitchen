package com.github.wallev.maidsoulkitchen.compat.msm.farmersdelight.handskillet;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.IdleStepUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FD_HAND_SKILLET)
public class GeneratorFdHandSkilletGuide implements ICookingRecipeGuideGenerator<CampfireCookingRecipe> {
//public class GeneratorFdHandSkilletGuide implements ICookingRecipeGuideGenerator<CampfireCookingRecipe, Container> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return false;
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() {
        return RecipeType.CAMPFIRE_COOKING;
    }


    @TypeLang(en_us = "Hand Skillet", zh_cn = "手持煎锅")
    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.FD, RecipeType.CAMPFIRE_COOKING + "_from_hand");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModTags.HEAT_SOURCES);
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, CampfireCookingRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        craftGuide.addStep(IdleStepUtil.makeStepWithLimit(pos));

        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                realItems,
                outputs,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(EnchantCommonUseAction.OPTION_USE_METHOD, EnchantCommonUseAction.USE_TYPE.LONG)
        ));
    }

    @Override
    public int getRecipeTime(CampfireCookingRecipe recipe) {
        return 0;
    }

    @Override
    public List<Ingredient> getInputs(CampfireCookingRecipe recipe) {
        List<Ingredient> inputs = Lists.newArrayList(Ingredient.of(ModItems.SKILLET.get()));
        inputs.addAll(recipe.getIngredients());
        return inputs;
    }

    @Override
    public List<ItemStack> getOutputs(CampfireCookingRecipe recipe, RegistryAccess registryAccess) {
        return Lists.newArrayList(recipe.getResultItem(registryAccess), ModItems.SKILLET.get().getDefaultInstance());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.SKILLET.get();
    }
}
