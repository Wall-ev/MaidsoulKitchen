package com.github.wallev.maidsoulkitchen.compat.msm.common.util.action;

import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.FailCraftGuideStepData;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;
import java.util.stream.IntStream;

import static com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil.makeTargetVirtualNoSide;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class ToolUseStepUtil {


    // ----------------------------------------- No Sneak Start -------------------------------------------------------------//
    // 必须层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStep(BlockPos pos, ItemStack tool) {
        return makeStep(pos, tool, List.of());
    }

    public static CraftGuideStepData makeStep(BlockPos pos, ItemStack tool, ItemStack result) {
        return makeStep(pos, tool, List.of(result));
    }

    /**
     * 创建一个物品使用步骤数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param tool 使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeStep(BlockPos pos, ItemStack tool, List<ItemStack> results) {
        List<ItemStack> allOutputs = Lists.newArrayList(results);
        allOutputs.add(tool);
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(tool),
                allOutputs,
                EnchantCommonUseAction.TYPE
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool) {
        steps.add(makeStep(pos, tool));
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, List<ItemStack> results) {
        steps.add(makeStep(pos, tool, results));
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, ItemStack result) {
        steps.add(makeStep(pos, tool, result));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(CraftGuideOperator2 craftGuide, BlockPos pos, ItemStack tool) {
        craftGuide.addStep(makeStep(pos, tool));
    }

    public static void addStep(CraftGuideOperator2 craftGuide, BlockPos pos, ItemStack tool, List<ItemStack> results) {
        craftGuide.addStep(makeStep(pos, tool, results));
    }

    public static void addStep(CraftGuideOperator2 craftGuide, BlockPos pos, ItemStack tool, ItemStack result) {
        craftGuide.addStep(makeStep(pos, tool, result));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 可选层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeOptionalStep(BlockPos pos, ItemStack tool) {
        return makeOptionalStep(pos, tool, List.of());
    }

    public static CraftGuideStepData makeOptionalStep(BlockPos pos, ItemStack tool, ItemStack result) {
        return makeOptionalStep(pos, tool, List.of(result));
    }

    /**
     * 创建一个可选物品使用步骤的数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param tool 需要使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeOptionalStep(BlockPos pos, ItemStack tool, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(tool),
                results,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(ActionOption.OPTIONAL, true)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(CraftGuideOperator2 craftGuide, BlockPos pos, ItemStack tool) {
        craftGuide.addStep(makeOptionalStep(pos, tool));
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuide, BlockPos pos, ItemStack tool, List<ItemStack> results) {
        craftGuide.addStep(makeOptionalStep(pos, tool, results));
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuide, BlockPos pos, ItemStack tool, ItemStack result) {
        craftGuide.addStep(makeOptionalStep(pos, tool, result));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, List<ItemStack> results) {
        steps.add(makeOptionalStep(pos, tool, results));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, ItemStack result) {
        steps.add(makeOptionalStep(pos, tool, result));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool) {
        steps.add(makeOptionalStep(pos, tool));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, ItemStack tool, ItemStack... backItems) {
        return makeStepIfFail_(pos, tool, List.of(), List.of(backItems));
    }

    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, ItemStack tool, ItemStack result, ItemStack... backItems) {
        return makeStepIfFail_(pos, tool, List.of(result), List.of(backItems));
    }

    public static CraftGuideStepData makeStepIfFailForSingleItem_(BlockPos pos, ItemStack tool, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .flatMap(itemStack -> {
                    return IntStream.range(0, itemStack.getCount() + 1)
                            .mapToObj(i -> itemStack.copyWithCount(1));
                })
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepIfFail(pos, tool, results, failSteps);
    }

    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, ItemStack tool, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepIfFail(pos, tool, results, failSteps);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail_(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack... backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), tool, backItems));
    }

    public static void addStepIfFail_(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result, ItemStack... backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), tool, result, backItems));
    }

    public static void addStepIfFailForSingleItem_(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results, List<ItemStack> backItems) {
        craftGuide.addStep(makeStepIfFailForSingleItem_(craftGuide.pos(), tool, results, backItems));
    }

    public static void addStepIfFail_(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results, List<ItemStack> backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), tool, results, backItems));
    }
    // -----------------------------------------------------------------------------------------------------------------------//



    
    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack tool, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, tool, List.of(), failSteps);
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack tool, ItemStack result, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, tool, List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack tool, List<ItemStack> results, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, tool, results, List.of(failSteps));
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack tool, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeStepIfFail(pos, tool, List.of(result), failSteps);
    }

    /**
     * 创建一个物品使用步骤数据对象，可添加失败回调步骤
     *
     * @param pos 目标位置坐标
     * @param tool 需要使用的物品列表
     * @param results 期望获得的结果物品列表
     * @param failSteps 失败时的步骤数据列表
     * @return CraftGuideStepData 物品使用步骤数据对象
     */
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack tool, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(tool),
                results,
                EnchantCommonUseAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), tool, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), tool, result, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result, List<CraftGuideStepData> failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), tool, result, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), tool, results, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), tool, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, tool, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, ItemStack result, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, tool, result, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, List<ItemStack> results, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, tool, results, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, tool, results, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, ItemStack result, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, tool, result, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//
    // ----------------------------------------- No Sneak End -------------------------------------------------------------//






    // ----------------------------------------- Need Sneak End -------------------------------------------------------------//
    // 必须层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepWithSneak(BlockPos pos, ItemStack tool) {
        return makeStepWithSneak(pos, tool, List.of());
    }

    public static CraftGuideStepData makeStepWithSneak(BlockPos pos, ItemStack tool, ItemStack result) {
        return makeStepWithSneak(pos, tool, List.of(result));
    }

    /**
     * 创建一个物品使用步骤数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param tool 使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeStepWithSneak(BlockPos pos, ItemStack tool, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(tool),
                results,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(EnchantCommonUseAction.SNEAK, true)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack tool) {
        craftGuide.addStep(makeStep(craftGuide.pos(), tool));
    }

    public static void addStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result) {
        craftGuide.addStep(makeStep(craftGuide.pos(), tool, result));
    }

    public static void addStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results) {
        craftGuide.addStep(makeStep(craftGuide.pos(), tool, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool) {
        steps.add(makeStep(pos, tool));
    }

    public static void addStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, ItemStack result) {
        steps.add(makeStep(pos, tool, result));
    }

    public static void addStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, List<ItemStack> results) {
        steps.add(makeStep(pos, tool, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 可选层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeOptionalStepWithSneak(BlockPos pos, ItemStack tool) {
        return makeOptionalStepWithSneak(pos, tool, List.of());
    }

    public static CraftGuideStepData makeOptionalStepWithSneak(BlockPos pos, ItemStack tool, ItemStack result) {
        return makeOptionalStepWithSneak(pos, tool, List.of(result));
    }

    /**
     * 创建一个可选物品使用步骤的数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param tool 需要使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeOptionalStepWithSneak(BlockPos pos, ItemStack tool, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(tool),
                results,
                EnchantCommonUseAction.TYPE,
                new ActionOptionSet(List.of(
                        new ActionOptionSet.ActionOptionSetItem<>(ActionOption.OPTIONAL, true),
                        new ActionOptionSet.ActionOptionSetItem<>(EnchantCommonUseAction.SNEAK, true)
                ))
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack tool) {
            craftGuide.addStep(makeOptionalStep(craftGuide.pos(), tool));
    }

    public static void addOptionalStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result) {
            craftGuide.addStep(makeOptionalStep(craftGuide.pos(), tool, result));
    }

    public static void addOptionalStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results) {
            craftGuide.addStep(makeOptionalStep(craftGuide.pos(), tool, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, List<ItemStack> results) {
            steps.add(makeOptionalStepWithSneak(pos, tool, results));
    }

    public static void addOptionalStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool) {
            steps.add(makeOptionalStepWithSneak(pos, tool));
    }

    public static void addOptionalStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool, ItemStack result) {
            steps.add(makeOptionalStepWithSneak(pos, tool, result));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepWithSneakIfFail_(BlockPos pos, ItemStack tool, ItemStack... backItems) {
        return makeStepWithSneakIfFail_(pos, tool, List.of(), List.of(backItems));
    }

    public static CraftGuideStepData makeStepWithSneakIfFail_(BlockPos pos, ItemStack tool, ItemStack result, ItemStack... backItems) {
        return makeStepWithSneakIfFail_(pos, tool, List.of(result), List.of(backItems));
    }

    public static CraftGuideStepData makeStepWithSneakIfFail_(BlockPos pos, ItemStack tool, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepWithSneakIfFail(pos, tool, results, failSteps);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void makeStepWithSneakIfFail_(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack... backItems) {
            craftGuide.addStep(makeStepWithSneakIfFail_(craftGuide.pos(), tool, backItems));
    }

    public static void makeStepWithSneakIfFail_(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result, ItemStack... backItems) {
            craftGuide.addStep(makeStepWithSneakIfFail_(craftGuide.pos(), tool, result, backItems));
    }

    public static void makeStepWithSneakIfFail_(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results, List<ItemStack> backItems) {
            craftGuide.addStep(makeStepWithSneakIfFail_(craftGuide.pos(), tool, results, backItems));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack tool, CraftGuideStepData... failSteps) {
        return makeStepWithSneakIfFail(pos, tool, List.of(), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack tool, ItemStack result, CraftGuideStepData... failSteps) {
        return makeStepWithSneakIfFail(pos, tool, List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack tool, List<ItemStack> results, CraftGuideStepData... failSteps) {
        return makeStepWithSneakIfFail(pos, tool, results, List.of(failSteps));
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack tool, List<CraftGuideStepData> failSteps) {
        return makeStepWithSneakIfFail(pos, tool, List.of(), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack tool, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeStepWithSneakIfFail(pos, tool, List.of(result), failSteps);
    }

    /**
     * 创建一个物品使用步骤数据对象，可添加失败回调步骤
     *
     * @param pos 目标位置坐标
     * @param tool 需要使用的物品列表
     * @param results 期望获得的结果物品列表
     * @param failSteps 失败时的步骤数据列表
     * @return CraftGuideStepData 物品使用步骤数据对象
     */
    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack tool, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        List<ItemStack> oResults = Lists.newArrayList(tool);
        oResults.addAll(results);
        CraftGuideStepData step = new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(tool),
                oResults,
                EnchantCommonUseAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        );
        ActionOptionSet.with(EnchantCommonUseAction.SNEAK, true).applyTo(step);
        return step;
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, CraftGuideStepData... failSteps) {
            craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), tool, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result, CraftGuideStepData... failSteps) {
            craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), tool, result, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results, CraftGuideStepData... failSteps) {
            craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), tool, results, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
            craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), tool, results, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, List<CraftGuideStepData> failSteps) {
            craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), tool, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack tool, ItemStack result, List<CraftGuideStepData> failSteps) {
            craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), tool, result, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//



    // ----------------------------------------- Need Sneak End -------------------------------------------------------------//

}
