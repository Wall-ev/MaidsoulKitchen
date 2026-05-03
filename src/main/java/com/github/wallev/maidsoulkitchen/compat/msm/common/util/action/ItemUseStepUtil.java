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

import static com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil.*;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class ItemUseStepUtil {


    // ----------------------------------------- No Sneak Start -------------------------------------------------------------//
    // 必须层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStep(BlockPos pos, ItemStack item) {
        return makeStep(pos, List.of(item), List.of());
    }

    public static CraftGuideStepData makeStep(BlockPos pos, ItemStack item, ItemStack result) {
        return makeStep(pos, List.of(item), List.of(result));
    }

    public static CraftGuideStepData makeStep(BlockPos pos, List<ItemStack> items, ItemStack result) {
        return makeStep(pos, items, List.of(result));
    }

    /**
     * 创建一个物品使用步骤数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param items 使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeStep(BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(CraftGuideOperator2 craftGuideOperator2, ItemStack item) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), List.of(item), List.of());
    }

    public static void addStep(CraftGuideOperator2 craftGuideOperator2, ItemStack item, ItemStack result) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), List.of(item), List.of(result));
    }

    public static void addStep(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items, ItemStack result) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items, List.of(result));
    }

    public static void addStep(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items, List<ItemStack> results) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items, results);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        addStep(steps, pos, List.of(item), List.of());
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result) {
        addStep(steps, pos, List.of(item), List.of(result));
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result) {
        addStep(steps, pos, items, List.of(result));
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        steps.add(makeStep(pos, items, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//





    // 可选层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeOptionalStep(BlockPos pos, ItemStack item) {
        return makeOptionalStep(pos, List.of(item), List.of());
    }

    public static CraftGuideStepData makeOptionalStep(BlockPos pos, ItemStack item, ItemStack result) {
        return makeOptionalStep(pos, List.of(item), List.of(result));
    }

    public static CraftGuideStepData makeOptionalStep(BlockPos pos, List<ItemStack> items, ItemStack result) {
        return makeOptionalStep(pos, items, List.of(result));
    }

    /**
     * 创建一个可选物品使用步骤的数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param items 需要使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeOptionalStep(BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(ActionOption.OPTIONAL, true)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(CraftGuideOperator2 craftGuideOperator2, ItemStack item) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), List.of(item), List.of());
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuideOperator2, ItemStack item, ItemStack result) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), List.of(item), List.of(result));
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items, ItemStack result) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items, List.of(result));
    }

    public static void addOptionalStep(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items, List<ItemStack> results) {
        addStep(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items, results);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        steps.add(makeOptionalStep(pos, List.of(item), List.of()));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result) {
        steps.add(makeOptionalStep(pos, List.of(item), List.of(result)));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result) {
        steps.add(makeOptionalStep(pos, items, List.of(result)));
    }

    public static void addOptionalStep(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        steps.add(makeOptionalStep(pos, items, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, ItemStack item, ItemStack... backItems) {
        return makeStepIfFail_(pos, List.of(item), List.of(), List.of(backItems));
    }

    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, ItemStack item, ItemStack result, ItemStack... backItems) {
        return makeStepIfFail_(pos, List.of(item), List.of(result), List.of(backItems));
    }

    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, List<ItemStack> items, ItemStack result, ItemStack... backItems) {
        return makeStepIfFail_(pos, items, List.of(result), List.of(backItems));
    }

    public static CraftGuideStepData makeStepIfFailForSingleItem_(BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .flatMap(itemStack -> {
                    return IntStream.range(0, itemStack.getCount() + 1)
                            .mapToObj(i -> itemStack.copyWithCount(1));
                })
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepIfFail(pos, items, results, failSteps);
    }

    public static CraftGuideStepData makeStepIfFail_(BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepIfFail(pos, items, results, failSteps);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail_(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack... backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), item, backItems));
    }

    public static void addStepIfFail_(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result, ItemStack... backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), item, result, backItems));
    }

    public static void addStepIfFail_(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result, ItemStack... backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), items, result, backItems));
    }

    public static void addStepIfFailForSingleItem_(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), items, results, backItems));
    }

    public static void addStepIfFail_(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        craftGuide.addStep(makeStepIfFail_(craftGuide.pos(), items, results, backItems));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail_(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack... backItems) {
        steps.add(makeStepIfFail_(pos, item, backItems));
    }

    public static void addStepIfFail_(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result, ItemStack... backItems) {
        steps.add(makeStepIfFail_(pos, item, result, backItems));
    }

    public static void addStepIfFail_(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result, ItemStack... backItems) {
        steps.add(makeStepIfFail_(pos, items, result, backItems));
    }

    public static void addStepIfFailForSingleItem_(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        steps.add(makeStepIfFailForSingleItem_(pos, items, results, backItems));
    }

    public static void addStepIfFail_(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        steps.add(makeStepIfFail_(pos, items, results, backItems));
    }
    // -----------------------------------------------------------------------------------------------------------------------//





    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack item, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, List.of(item), List.of(), failSteps);
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, List.of(item), List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, items, List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, List<ItemStack> items, List<ItemStack> results, CraftGuideStepData... failSteps) {
        return makeStepIfFail(pos, items, results, List.of(failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack item, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), item, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), item, result, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), items, result, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), items, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, item, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, item, result, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, items, result, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results, CraftGuideStepData... failSteps) {
        steps.add(makeStepIfFail(pos, items, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack item, List<CraftGuideStepData> failSteps) {
        return makeStepIfFail(pos, List.of(item), List.of(), failSteps);
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, ItemStack item, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeStepIfFail(pos, List.of(item), List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepIfFail(BlockPos pos, List<ItemStack> items, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeStepIfFail(pos, items, List.of(result), failSteps);
    }

    /**
     * 创建一个物品使用步骤数据对象，可添加失败回调步骤
     *
     * @param pos 目标位置坐标
     * @param items 需要使用的物品列表
     * @param results 期望获得的结果物品列表
     * @param failSteps 失败时的步骤数据列表
     * @return CraftGuideStepData 物品使用步骤数据对象
     */
    public static CraftGuideStepData makeStepIfFail(BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack item, List<CraftGuideStepData> failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), item, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result, List<CraftGuideStepData> failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), item, result, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result, List<CraftGuideStepData> failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), items, result, failSteps));
    }

    public static void addStepIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        craftGuide.addStep(makeStepIfFail(craftGuide.pos(), items, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, item, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, item, result, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, items, result, failSteps));
    }

    public static void addStepIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepIfFail(pos, items, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//

    // ----------------------------------------- No Sneak End -------------------------------------------------------------//



    // ----------------------------------------- Need Sneak End -------------------------------------------------------------//
    // 必须层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepWithSneak(BlockPos pos, ItemStack item) {
        return makeStepWithSneak(pos, List.of(item), List.of());
    }

    public static CraftGuideStepData makeStepWithSneak(BlockPos pos, ItemStack item, ItemStack result) {
        return makeStepWithSneak(pos, List.of(item), List.of(result));
    }

    public static CraftGuideStepData makeStepWithSneak(BlockPos pos, List<ItemStack> items, ItemStack result) {
        return makeStepWithSneak(pos, items, List.of(result));
    }

    /**
     * 创建一个物品使用步骤数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param items 使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeStepWithSneak(BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(EnchantCommonUseAction.SNEAK, true)
        );
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        steps.add(makeStepWithSneak(pos, item));
    }

    public static void addStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result) {
        steps.add(makeStepWithSneak(pos, item, result));
    }

    public static void addStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result) {
        steps.add(makeStepWithSneak(pos, items, result));
    }

    public static void addStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        steps.add(makeStepWithSneak(pos, items, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack item) {
        addStepWithSneak(craftGuide.steps(), craftGuide.pos(), item);
    }

    public static void addStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result) {
        addStepWithSneak(craftGuide.steps(), craftGuide.pos(), item, result);
    }

    public static void addStepWithSneak(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result) {
        addStepWithSneak(craftGuide.steps(), craftGuide.pos(), items, result);
    }

    public static void addStepWithSneak(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results) {
        addStepWithSneak(craftGuide.steps(), craftGuide.pos(), items, results);
    }
    // -----------------------------------------------------------------------------------------------------------------------//



    // 可选层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeOptionalStepWithSneak(BlockPos pos, ItemStack item) {
        return makeOptionalStepWithSneak(pos, List.of(item), List.of());
    }

    public static CraftGuideStepData makeOptionalStepWithSneak(BlockPos pos, ItemStack item, ItemStack result) {
        return makeOptionalStepWithSneak(pos, List.of(item), List.of(result));
    }

    public static CraftGuideStepData makeOptionalStepWithSneak(BlockPos pos, List<ItemStack> items, ItemStack result) {
        return makeOptionalStepWithSneak(pos, items, List.of(result));
    }

    /**
     * 创建一个可选物品使用步骤的数据对象
     *
     * @param pos 物品使用的位置坐标
     * @param items 需要使用的物品列表
     * @param results 使用后产生的结果物品列表
     * @return 返回构建好的工艺指导步骤数据对象
     */
    public static CraftGuideStepData makeOptionalStepWithSneak(BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                items,
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
    public static void addOptionalStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack item) {
        craftGuide.addStep(makeOptionalStepWithSneak(craftGuide.pos(), item));
    }

    public static void addOptionalStepWithSneak(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result) {
        craftGuide.addStep(makeOptionalStepWithSneak(craftGuide.pos(), item, result));
    }

    public static void addOptionalStepWithSneak(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result) {
        craftGuide.addStep(makeOptionalStepWithSneak(craftGuide.pos(), items, result));
    }

    public static void addOptionalStepWithSneak(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results) {
        craftGuide.addStep(makeOptionalStepWithSneak(craftGuide.pos(), items, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addOptionalStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        steps.add(makeOptionalStepWithSneak(pos, item));
    }

    public static void addOptionalStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result) {
        steps.add(makeOptionalStepWithSneak(pos, item, result));
    }

    public static void addOptionalStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result) {
        steps.add(makeOptionalStepWithSneak(pos, items, result));
    }

    public static void addOptionalStepWithSneak(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        steps.add(makeOptionalStepWithSneak(pos, items, results));
    }
    // -----------------------------------------------------------------------------------------------------------------------//



    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepWithSneakIfFail_(BlockPos pos, ItemStack item, ItemStack... backItems) {
        return makeStepWithSneakIfFail_(pos, List.of(item), List.of(), List.of(backItems));
    }

    public static CraftGuideStepData makeStepWithSneakIfFail_(BlockPos pos, ItemStack item, ItemStack result, ItemStack... backItems) {
        return makeStepWithSneakIfFail_(pos, List.of(item), List.of(result), List.of(backItems));
    }

    public static CraftGuideStepData makeStepWithSneakIfFail_(BlockPos pos, List<ItemStack> items, ItemStack result, ItemStack... backItems) {
        return makeStepWithSneakIfFail_(pos, items, List.of(result), List.of(backItems));
    }

    public static CraftGuideStepData makeStepWithSneakIfFail_(BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .map(itemStack -> EmptyUseStepUtil.makeOptionalStep(pos, itemStack))
                .toList();

        return makeStepWithSneakIfFail(pos, items, results, failSteps);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneakIfFail_(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack... backItems) {
        steps.add(makeStepWithSneakIfFail_(pos, item, backItems));
    }

    public static void addStepWithSneakIfFail_(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result, ItemStack... backItems) {
        steps.add(makeStepWithSneakIfFail_(pos, item, result, backItems));
    }

    public static void addStepWithSneakIfFail_(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result, ItemStack... backItems) {
        steps.add(makeStepWithSneakIfFail_(pos, items, result, backItems));
    }

    public static void addStepWithSneakIfFail_(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        steps.add(makeStepWithSneakIfFail_(pos, items, results, backItems));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneakIfFail_(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack... backItems) {
        craftGuide.addStep(makeStepWithSneakIfFail_(craftGuide.pos(), item, backItems));
    }

    public static void addStepWithSneakIfFail_(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result, ItemStack... backItems) {
        craftGuide.addStep(makeStepWithSneakIfFail_(craftGuide.pos(), item, result, backItems));
    }

    public static void addStepWithSneakIfFail_(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result, ItemStack... backItems) {
        craftGuide.addStep(makeStepWithSneakIfFail_(craftGuide.pos(), items, result, backItems));
    }

    public static void addStepWithSneakIfFail_(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
        craftGuide.addStep(makeStepWithSneakIfFail_(craftGuide.pos(), items, results, backItems));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack item, CraftGuideStepData... failSteps) {
        return makeStepWithSneakIfFail(pos, List.of(item), List.of(), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        return makeStepWithSneakIfFail(pos, List.of(item), List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
        return makeStepWithSneakIfFail(pos, items, List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, List<ItemStack> items, List<ItemStack> results, CraftGuideStepData... failSteps) {
        return makeStepWithSneakIfFail(pos, items, results, List.of(failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack item, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), item, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), item, result, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), items, result, failSteps));
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results, CraftGuideStepData... failSteps) {
        craftGuide.addStep(makeStepWithSneakIfFail(craftGuide.pos(), items, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, CraftGuideStepData... failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, item, failSteps));
    }

    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, item, result, failSteps));
    }

    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, items, result, failSteps));
    }

    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results, CraftGuideStepData... failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, items, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//




    // 失败层
    // -----------------------------------------------------------------------------------------------------------------------//
    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack item, List<CraftGuideStepData> failSteps) {
        return makeStepWithSneakIfFail(pos, List.of(item), List.of(), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, ItemStack item, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeStepWithSneakIfFail(pos, List.of(item), List.of(result), failSteps);
    }

    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, List<ItemStack> items, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeStepWithSneakIfFail(pos, items, List.of(result), failSteps);
    }

    /**
     * 创建一个物品使用步骤数据对象，可添加失败回调步骤
     *
     * @param pos 目标位置坐标
     * @param items 需要使用的物品列表
     * @param results 期望获得的结果物品列表
     * @param failSteps 失败时的步骤数据列表
     * @return CraftGuideStepData 物品使用步骤数据对象
     */
    public static CraftGuideStepData makeStepWithSneakIfFail(BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        CraftGuideStepData step = new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        );
        ActionOptionSet.with(EnchantCommonUseAction.SNEAK, true).applyTo(step);
        return step;
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, item, failSteps));
    }

    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, item, result, failSteps));
    }

    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, ItemStack result, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, items, result, failSteps));
    }

    public static void addStepWithSneakIfFail(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        steps.add(makeStepWithSneakIfFail(pos, items, results, failSteps));
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // -----------------------------------------------------------------------------------------------------------------------//
    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack item, List<CraftGuideStepData> failSteps) {
        addStepWithSneakIfFail(craftGuide.steps(), craftGuide.pos(), item, failSteps);
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, ItemStack item, ItemStack result, List<CraftGuideStepData> failSteps) {
        addStepWithSneakIfFail(craftGuide.steps(), craftGuide.pos(), item, result, failSteps);
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, ItemStack result, List<CraftGuideStepData> failSteps) {
        addStepWithSneakIfFail(craftGuide.steps(), craftGuide.pos(), items, result, failSteps);
    }

    public static void addStepWithSneakIfFail(CraftGuideOperator2 craftGuide, List<ItemStack> items, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        addStepWithSneakIfFail(craftGuide.steps(), craftGuide.pos(), items, results, failSteps);
    }
    // -----------------------------------------------------------------------------------------------------------------------//


    // ----------------------------------------- Need Sneak End -------------------------------------------------------------//

}
