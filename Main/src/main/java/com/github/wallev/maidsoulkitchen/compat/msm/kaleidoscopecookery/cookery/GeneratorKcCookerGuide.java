package com.github.wallev.maidsoulkitchen.compat.msm.kaleidoscopecookery.cookery;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonIdleAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.SneakCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.IdleStepUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.ItemUseStepUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.ToolUseStepUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.CraftAction;
import studio.fantasyit.maid_storage_manager.craft.action.PathTargetLocator;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.*;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_KC_COOKER)
public class GeneratorKcCookerGuide implements ILdCookingPotGuideGenerator<PotRecipe, PotBlockEntity> {
    public GeneratorKcCookerGuide(CollectCraftEvent event) {
        event.addAutoCraftGuideGenerator(this);
        event.addAction(
                CookerAction.TYPE,
                CookerAction::new,
                PathTargetLocator::touchPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                CraftAction.MARK_HAND_RELATED,
                4,
                4,
                List.of(ActionOption.OPTIONAL, SneakCommonUseAction.OPTION_USE_METHOD, SneakCommonUseAction.SNEAK, EnchantCommonIdleAction.OPTION_WAIT)
        );
    }

    private static boolean isHit(PotBlockEntity be) {
        Level level = be.getLevel();
        assert level != null;
        BlockPos blockPos = be.getBlockPos();
        return level.getBlockState(blockPos.below()).getOptionalValue(BlockStateProperties.LIT).orElse(false);
    }

    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof PotBlockEntity;
    }

    @Override
    public boolean isHeated(PotBlockEntity be) {
        return isHit(be);
    }

    @Override
    public List<Ingredient> getInputs(PotRecipe recipe) {
        List<Ingredient> ingredients = Lists.newArrayList();
        ingredients.add(Ingredient.of(ModItems.KITCHEN_SHOVEL.get()));
        ingredients.add(Ingredient.of(ModItems.OIL.get()));
        NonNullList<Ingredient> ingredients1 = recipe.getIngredients();
        for (Ingredient ingredient : ingredients1) {
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    @Override
    public List<Ingredient> getContainers(PotRecipe recipe) {
        return List.of(recipe.carrier());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.POT.get();
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, PotRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        generateSteps1(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
//        generateSteps2(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
    }

    @Override
    public List<ItemStack> getRemains(PotRecipe recipe, List<ItemStack> inputs) {
        return List.of();
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public RecipeType<PotRecipe> getRecipeType() {
        return ModRecipes.POT_RECIPE;
    }

    public void generateSteps1(BlockPos pos, Level level, PotRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ItemStack kitchenShovel = realItems.remove(0);
        ItemStack container = ItemStack.EMPTY;
        List<ItemStack> inputsWithOil = Lists.newArrayList(realItems);
        if (needContainer) {
            container = containers.get(0);
        }
        craftGuide.addIdle(pos.above());

        // 放油和食材
        craftGuide.addItemUse(inputsWithOil);

        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(kitchenShovel),
                List.of(kitchenShovel),
                CookerAction.TYPE,
                CookerAction.CookerTime.toCompoundTag(recipe.time(), recipe.stirFryCount())
        ));

        CraftGuideStepData failStep = ItemUseStepUtil.makeOptionalStep(pos, Items.BOWL.getDefaultInstance());
        if (needContainer) {
            craftGuide.addItemUseIfFail(container, outputs, failStep);
        } else {
            craftGuide.addSneakToolUseIfFail(kitchenShovel, outputs, failStep);
        }
    }

    protected void generateSteps2(BlockPos pos, Level level, PotRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ItemStack kitchenShovel = realItems.remove(0);
        ItemStack container = ItemStack.EMPTY;
        List<ItemStack> inputsWithOil = Lists.newArrayList(realItems);
        if (needContainer) {
            container = containers.get(0);
        }

        // 放油和食材
        craftGuide.addItemUse(inputsWithOil);
//
//        craftGuide.addStep(new CraftGuideStepData(
//                TargetUtil.makeTargetVirtualNoSide(pos),
//                List.of(kitchenShovel),
//                List.of(kitchenShovel),
//                CookerAction.TYPE,
//                ActionOptionSet.with(EnchantCommonIdleAction.OPTION_WAIT, true, String.valueOf(recipe.time()))
//        ));
//
        List<Integer> stirFryTimes = Lists.newArrayList();

        Random random = new Random();
        int time = recipe.time() / 3;
        int stirFryMinCount = recipe.stirFryCount();
        int stirFrySpace = (time - 20) / stirFryMinCount;
        List<CraftGuideStepData> stirFrySteps = Lists.newArrayList();
        for (int tick = 0; tick < time + 20; tick++) {
            if (tick % stirFrySpace == 0) {
                stirFrySteps.add(ToolUseStepUtil.makeStep(pos, kitchenShovel));

                stirFryTimes.add(tick);
            } else {
                if (tick % 2 == 0) {
                    int nextInt = random.nextInt(1, 10);
                    if (tick % nextInt == 0) {
                        stirFrySteps.add(ToolUseStepUtil.makeStep(pos, kitchenShovel));

                        stirFryTimes.add(tick);
                        continue;
                    }
                }

                stirFrySteps.add(IdleStepUtil.makeStep(pos));
            }
        }

// 存储连续idle步骤的起始和结束位置
        List<Pair<Integer, Integer>> idleIntervals = new ArrayList<>();
        Integer idleStart = null;

        for (int i = 0; i < stirFrySteps.size(); i++) {
            CraftGuideStepData step = stirFrySteps.get(i);

            // 判断当前步骤是否为idle step
            boolean isIdle = step.action == EnchantCommonIdleAction.TYPE;

            if (isIdle) {
                // 如果是idle step且尚未记录起始位置，则记录
                if (idleStart == null) {
                    idleStart = i;
                }
            } else {
                // 如果不是idle step但之前有记录起始位置，则结束当前连续idle
                if (idleStart != null) {
                    idleIntervals.add(Pair.of(idleStart, i - 1));
                    idleStart = null;
                }
            }
        }

        // 处理最后可能的连续idle（如果列表以idle step结束）
        if (idleStart != null) {
            idleIntervals.add(Pair.of(idleStart, stirFrySteps.size() - 1));
        }

        List<CraftGuideStepData> allSteps = Lists.newArrayList();
        int lastIndex = 0;
        for (Pair<Integer, Integer> pair : idleIntervals) {
            int first = pair.getFirst();
            int second = pair.getSecond();

            for (int i = 0; i < first - lastIndex - 1; i++) {
                allSteps.add(ToolUseStepUtil.makeStep(pos, kitchenShovel));
            }
            allSteps.add(IdleStepUtil.makeStep(pos, second - first));

            lastIndex = second;
        }

        craftGuide.steps().addAll(allSteps);
//        craftGuide.steps().addAll(stirFrySteps);


        craftGuide.addIdle(30);


        CraftGuideStepData failStep = ItemUseStepUtil.makeOptionalStep(pos, Items.BOWL.getDefaultInstance());
        if (needContainer) {
            craftGuide.addItemUseIfFail(container, outputs, failStep);
        } else {
            craftGuide.addSneakToolUseIfFail(kitchenShovel, outputs, failStep);
        }
    }
}
