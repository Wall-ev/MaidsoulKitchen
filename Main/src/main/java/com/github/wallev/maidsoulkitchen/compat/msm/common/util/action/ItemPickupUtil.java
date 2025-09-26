package com.github.wallev.maidsoulkitchen.compat.msm.common.util.action;

import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonPickupItemAction;
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

import static com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil.makeTargetVirtualNoSide;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class ItemPickupUtil {

    // -----------------------------------------------------------------------------------------------------------------------//

    public static CraftGuideStepData makeStep(BlockPos pos, ItemStack... result) {
        return makeStep(pos, List.of(result));
    }

    /**
     * 创建一个合成指南步骤数据对象
     *
     * @param pos 方块位置，用于确定目标位置
     * @param results 物品栈列表，包含该步骤涉及的物品
     * @return 返回新创建的CraftGuideStepData对象
     */
    public static CraftGuideStepData makeStep(BlockPos pos, List<ItemStack> results) {
         return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),
                results,
                EnchantCommonPickupItemAction.TYPE
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(CraftGuideOperator2 craftGuide, ItemStack... result) {
        addStep(craftGuide.steps(), craftGuide.pos(), result);
    }

    public static void addStep(CraftGuideOperator2 craftGuide, List<ItemStack> items) {
        addStep(craftGuide.steps(), craftGuide.pos(), items);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack... result) {
        steps.add(makeStep(pos, result));
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        steps.add(makeStep(pos, items));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // -----------------------------------------------------------------------------------------------------------------------//

    public static CraftGuideStepData makeOptionalStep(BlockPos pos, ItemStack... result) {
        return makeOptionalStep(pos, List.of(result));
    }

    /**
     * 创建一个可选的工艺指导步骤数据对象
     *
     * @param pos 方块位置
     * @param items 物品堆列表
     * @return CraftGuideStepData 工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeOptionalStep(BlockPos pos, List<ItemStack> items) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),
                items,
                EnchantCommonPickupItemAction.TYPE,
                ActionOptionSet.with(ActionOption.OPTIONAL, true)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack... result) {
        steps.add(makeOptionalStep(pos, result));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        steps.add(makeOptionalStep(pos, items));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(CraftGuideOperator2 craftGuide, ItemStack... result) {
        addOptionalStep(craftGuide.steps(), craftGuide.pos(), result);
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuide, List<ItemStack> items) {
        addOptionalStep(craftGuide.steps(), craftGuide.pos(), items);
    }
    // -----------------------------------------------------------------------------------------------------------------------//




//
//    // 失败层
//    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, ItemStack result, ItemStack... backItems) {
//        return makeStepIfFail_(pos, List.of(result), List.of(backItems));
//    }
//
//    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, List<ItemStack> results, List<ItemStack> backItems) {
//        List<CraftGuideStepData> failSteps = backItems.stream()
//                .map(itemStack -> EmptyStepUtil.makeOptionalStep(pos, itemStack))
//                .toList();
//
//        return makeStepIfFail(pos, results, failSteps);
//    }


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
     * 当合成步骤失败时创建一个新的合成指南步骤数据对象
     *
     * @param pos 方块位置信息
     * @param items 物品堆列表
     * @param failSteps 失败时的合成步骤列表
     * @return 返回新创建的CraftGuideStepData对象，用于处理合成失败的情况
     */
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, List<ItemStack> items, List<CraftGuideStepData> failSteps) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),
                items,
                EnchantCommonPickupItemAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack result, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, result, List.of(failSteps)));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> results, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, results, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack result, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, result, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, items, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack result, CraftGuideStepData... failSteps) {
        addStepIfFail(craftGuide.steps(), craftGuide.pos(), result, failSteps);
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> results, CraftGuideStepData... failSteps) {
        addStepIfFail(craftGuide.steps(), craftGuide.pos(), results, failSteps);
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack result, List<CraftGuideStepData> failSteps) {
        addStepIfFail(craftGuide.steps(), craftGuide.pos(), result, failSteps);
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<CraftGuideStepData> failSteps) {
        addStepIfFail(craftGuide.steps(), craftGuide.pos(), items, failSteps);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


}
