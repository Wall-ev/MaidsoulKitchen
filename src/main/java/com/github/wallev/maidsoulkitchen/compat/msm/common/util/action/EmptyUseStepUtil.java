package com.github.wallev.maidsoulkitchen.compat.msm.common.util.action;

import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.FailCraftGuideStepData;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;
import java.util.stream.IntStream;

import static com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil.makeTargetVirtualNoSide;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class EmptyUseStepUtil {

    // ----------------------------------------- Make Empty Step Start -------------------------------------------------------------//
    // 必须层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStep(BlockPos pos) {
        return makeStep(pos, List.of());
    }

    public static CraftGuideStepData makeStep(BlockPos pos, ItemStack... result) {
        return makeStep(pos, List.of(result));
    }

    /**
     * 创建一个空物品使用步骤数据对象（无输入物品）
     *
     * @param pos 步骤执行的位置坐标
     * @param results 步骤产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeStep(BlockPos pos, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),  // 空输入物品列表
                results,
                EnchantCommonUseAction.TYPE
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos) {
        steps.add(makeStep(pos));
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack... result) {
        steps.add(makeStep(pos, result));
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        steps.add(makeStep(pos, items));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(CraftGuideOperator2 craftGuide) {
        craftGuide.addStep(makeStep(craftGuide.pos()));
    }

    public static void addStep(CraftGuideOperator2 craftGuide, ItemStack... result) {
        craftGuide.addStep(makeStep(craftGuide.pos(), result));
    }

    public static void addStep(CraftGuideOperator2 craftGuide, List<ItemStack> items) {
        craftGuide.addStep(makeStep(craftGuide.pos(), items));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 可选层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeOptionalStep(BlockPos pos) {
        return makeOptionalStep(pos, List.of());
    }

    public static CraftGuideStepData makeOptionalStep(BlockPos pos, ItemStack... result) {
        return makeOptionalStep(pos, List.of(result));
    }

    /**
     * 创建一个可选的空物品使用步骤数据对象（无输入物品）
     *
     * @param pos 步骤执行的位置坐标
     * @param results 步骤产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeOptionalStep(BlockPos pos, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),  // 空输入物品列表
                results,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(ActionOption.OPTIONAL, true)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos) {
        steps.add(makeOptionalStep(pos));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack... result) {
        steps.add(makeOptionalStep(pos, result));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        steps.add(makeOptionalStep(pos, items));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(CraftGuideOperator2 craftGuide) {
        craftGuide.addStep(makeOptionalStep(craftGuide.pos()));
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuide, ItemStack... result) {
        craftGuide.addStep(makeOptionalStep(craftGuide.pos(), result));
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuide, List<ItemStack> items) {
        craftGuide.addStep(makeOptionalStep(craftGuide.pos(), items));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, ItemStack result, ItemStack... backItems) {
        return makeStepIfFail_(pos, List.of(result), List.of(backItems));
    }

    public static CraftGuideStepData makeStepIfFailForSingleItem_(BlockPos pos, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .flatMap(itemStack -> {
                    return IntStream.range(0, itemStack.getCount() + 1)
                            .mapToObj(i -> itemStack.copyWithCount(1));
                })
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepIfFail(pos, results, failSteps);
    }

    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepIfFail(pos, results, failSteps);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail_(List<CraftGuideStepData> steps, BlockPos pos, ItemStack result, ItemStack... backItems) {
        steps.add(makeStepIfFail_(pos, result, backItems));
    }

    public static void addStepIfFailForSingleItem_(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> results, List<ItemStack> backItems) {
        steps.add(makeStepIfFailForSingleItem_(pos, results, backItems));
    }

    public static void addStepIfFail_(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> results, List<ItemStack> backItems) {
        steps.add(makeStepIfFail_(pos, results, backItems));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack result, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, List.of(result), List.of(failSteps));
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, List<ItemStack> results, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, results, List.of(failSteps));
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeStepIfFail(pos, List.of(result), failSteps);
    }

    /**
     * 创建一个空物品使用步骤数据对象（无输入物品），可添加失败回调步骤
     *
     * @param pos 步骤执行的位置坐标
     * @param results 步骤产生的结果物品列表
     * @param failSteps 失败时的步骤数据列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),  // 空输入物品列表
                results,
                EnchantCommonUseAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack result, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, result, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> results, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, results, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack result, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), result, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> results, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), results, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // ----------------------------------------- Make Empty Step End -------------------------------------------------------------//
}
