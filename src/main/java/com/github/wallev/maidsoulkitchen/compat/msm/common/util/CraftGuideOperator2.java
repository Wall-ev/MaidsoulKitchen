package com.github.wallev.maidsoulkitchen.compat.msm.common.util;

import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.*;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.FailCraftGuideStepData;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.IdleStepUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.ItemPickupUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.ItemUseStepUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.type.CommonType;
import studio.fantasyit.maid_storage_manager.storage.ItemHandler.ItemHandlerStorage;
import studio.fantasyit.maid_storage_manager.storage.Target;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 步骤生成器
 * <p>此处的步骤都来源这个包下：{@link com.github.wallev.maidsoulkitchen.compat.msm.common.util.action}
 *
 * @param steps 步骤
 * @param pos 方块坐标
 */
@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public record CraftGuideOperator2(List<CraftGuideStepData> steps, BlockPos pos) {
    @Deprecated
    public static final CraftGuideOperator2 INSTANCE = new CraftGuideOperator2();

    public CraftGuideOperator2(BlockPos pos) {
        this(Lists.newArrayList(), pos);
    }

    private CraftGuideOperator2() {
        this(Lists.newArrayList(), BlockPos.ZERO);
    }

    public static CraftGuideOperator2 create(BlockPos pos) {
        return new CraftGuideOperator2(pos);
    }

    public void addStep(CraftGuideStepData step) {
        steps.add(step);
    }

    public CraftGuideData makeCraftGuideData() {
        return makeCraftGuideData(CommonType.TYPE);
    }

    public CraftGuideData makeCraftGuideData(ResourceLocation id) {
        return new CraftGuideData(steps, id);
    }




    // ----------------------------------------- Item Take Start -------------------------------------------------------------//
    public void addItemTake(ItemStack item) {
        addItemTake(this, item);
    }

    public void addItemTake(BlockPos pos, ItemStack item) {
        addItemTake(this.steps, pos, item);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2, ItemStack item) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(), item);
    }

    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        addItemTake(steps, pos, List.of(item));
    }


    public void addItemTake(@Nullable Direction direction, ItemStack item) {
        addItemTake(this, direction, item);
    }

    public void addItemTake(BlockPos pos, @Nullable Direction direction, ItemStack item) {
        addItemTake(this.steps, pos, direction, item);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2, @Nullable Direction direction, ItemStack item) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(), direction, item);
    }

    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, @Nullable Direction direction, ItemStack item) {
        addItemTake(steps, pos, direction, List.of(item));
    }


    public void addItemTake(ResourceLocation storageType, ItemStack item) {
        addItemTake(this, storageType, item);
    }

    public void addItemTake(BlockPos pos, ResourceLocation storageType, ItemStack item) {
        addItemTake(this.steps, pos, storageType, item);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2, ResourceLocation storageType, ItemStack item) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, item);
    }

    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, ItemStack item) {
        addItemTake(steps, pos, storageType, List.of(item));
    }


    public void addItemTake(ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemTake(this, storageType, direction, item);
    }

    public void addItemTake(BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemTake(this.steps, pos, storageType, direction, item);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2, ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, direction, item);
    }

    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemTake(steps, pos, storageType, direction, List.of(item));
    }


    public void addItemTake(List<ItemStack> items) {
        addItemTake(this, items);
    }

    public void addItemTake(BlockPos pos, List<ItemStack> items) {
        addItemTake(this.steps, pos, items);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items);
    }

    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        addItemTake(steps, pos, Direction.DOWN, items);
    }


    public void addItemTake(@Nullable Direction direction, List<ItemStack> items) {
        addItemTake(this, direction, items);
    }

    public void addItemTake(BlockPos pos, @Nullable Direction direction, List<ItemStack> items) {
        addItemTake(this.steps, pos, direction, items);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2, @Nullable Direction direction, List<ItemStack> items) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(),  direction, items);
    }

    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, @Nullable Direction direction, List<ItemStack> items) {
        addItemTake(steps, pos, ItemHandlerStorage.TYPE, direction, items);
    }


    public void addItemTake(ResourceLocation storageType, List<ItemStack> items) {
        addItemTake(this, storageType, items);
    }

    public void addItemTake(BlockPos pos, ResourceLocation storageType, List<ItemStack> items) {
        addItemTake(this.steps, pos, storageType, items);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2, ResourceLocation storageType, List<ItemStack> items) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, items);
    }

    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, List<ItemStack> items) {
        addItemTake(steps, pos, storageType, Direction.DOWN, items);
    }


    public void addItemTake(ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> items) {
        addItemTake(this, storageType, direction, items);
    }

    public void addItemTake(BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> items) {
        addItemTake(this.steps, pos, storageType, direction, items);
    }

    public static void addItemTake(CraftGuideOperator2 craftGuideOperator2,  ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> outputs) {
        addItemTake(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, direction, outputs);
    }

    /**
     * 添加物品取出步骤到工艺指南步骤列表中
     *
     * @param steps 工艺指南步骤列表，用于存储生成的步骤数据
     * @param pos 方块位置，指定物品存储的位置
     * @param storageType 存储类型，用于标识物品存储的类型
     * @param direction 方向信息，可为空，指定存储的方向
     * @param outputs 输出物品列表，包含需要取出的物品
     */
    public static void addItemTake(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> outputs) {
        // 遍历输出物品列表，每3个物品为一组创建取出步骤
        forEach3Items(outputs, (oItems) -> {
            steps.add(new CraftGuideStepData(
                    TargetUtil.makeTarget(storageType, pos, direction),
                    List.of(),
                    oItems,
                    EnchantCommonTakeItemAction.TYPE
            ));
        });
    }

    // ----------------------------------------- Item Take End -------------------------------------------------------------//


    // ----------------------------------------- Item Insert Start -------------------------------------------------------------//
    public void addItemInsert(ItemStack item) {
        addItemInsert(this, item);
    }

    public void addItemInsert(BlockPos pos, ItemStack item) {
        addItemInsert(this.steps, pos, item);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2, ItemStack item) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), item);
    }

    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        addItemInsert(steps, pos, List.of(item));
    }


    public void addItemInsert(@Nullable Direction direction, ItemStack item) {
        addItemInsert(this, direction, item);
    }

    public void addItemInsert(BlockPos pos, @Nullable Direction direction, ItemStack item) {
        addItemInsert(this.steps, pos, direction, item);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2, @Nullable Direction direction, ItemStack item) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), direction, item);
    }

    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, @Nullable Direction direction, ItemStack item) {
        addItemInsert(steps, pos, direction, List.of(item));
    }


    public void addItemInsert(ResourceLocation storageType, ItemStack item) {
        addItemInsert(this, storageType, item);
    }

    public void addItemInsert(BlockPos pos, ResourceLocation storageType, ItemStack item) {
        addItemInsert(this.steps, pos, storageType, item);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2,  ResourceLocation storageType, ItemStack item) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, item);
    }

    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, ItemStack item) {
        addItemInsert(steps, pos, storageType, List.of(item));
    }


    public void addItemInsert(ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemInsert(this, storageType, direction, item);
    }

    public void addItemInsert(BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemInsert(this.steps, pos, storageType, direction, item);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2, ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, direction, item);
    }

    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, ItemStack item) {
        addItemInsert(steps, pos, storageType, direction, List.of(item));
    }


    public void addItemInsert(List<ItemStack> items) {
        addItemInsert(this, items);
    }

    public void addItemInsert(BlockPos pos, List<ItemStack> items) {
        addItemInsert(this.steps, pos, items);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items);
    }

    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        addItemInsert(steps, pos, (Direction) null, items);
    }


    public void addItemInsert(@Nullable Direction direction, List<ItemStack> items) {
        addItemInsert(this, direction, items);
    }

    public void addItemInsert(BlockPos pos, @Nullable Direction direction, List<ItemStack> items) {
        addItemInsert(this.steps, pos, direction, items);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2, @Nullable Direction direction, List<ItemStack> items) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), direction, items);
    }

    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, @Nullable Direction direction, List<ItemStack> items) {
        addItemInsert(steps, pos, ItemHandlerStorage.TYPE, direction, items);
    }


    public void addItemInsert(ResourceLocation storageType, List<ItemStack> items) {
        addItemInsert(this, storageType, items);
    }

    public void addItemInsert(BlockPos pos, ResourceLocation storageType, List<ItemStack> items) {
        addItemInsert(this.steps, pos, storageType, items);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2, ResourceLocation storageType, List<ItemStack> items) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, items);
    }

    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, List<ItemStack> items) {
        addItemInsert(steps, pos, storageType, null, items);
    }


    public void addItemInsert(ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> items) {
        addItemInsert(this, storageType, direction, items);
    }

    public void addItemInsert(BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> items) {
        addItemInsert(this.steps, pos, storageType, direction, items);
    }

    public static void addItemInsert(CraftGuideOperator2 craftGuideOperator2, ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> items) {
        addItemInsert(craftGuideOperator2.steps(), craftGuideOperator2.pos(), storageType, direction, items);
    }

    /**
     * 向步骤列表中添加物品插入操作的引导步骤
     *
     * @param steps 步骤数据列表，用于存储引导步骤
     * @param pos 方块位置，指定物品插入的目标位置
     * @param storageType 存储类型，用于标识目标容器的类型
     * @param direction 方向信息，可为空，指定物品插入的方向
     * @param items 物品堆列表，包含需要插入的物品
     */
    public static void addItemInsert(List<CraftGuideStepData> steps, BlockPos pos, ResourceLocation storageType, @Nullable Direction direction, List<ItemStack> items) {
        // 将物品列表按每3个物品一组进行分组处理，为每组创建相应的引导步骤
        forEach3Items(items, (oItems) -> {
            steps.add(new CraftGuideStepData(
                    TargetUtil.makeTarget(storageType, pos, direction),
                    oItems,
                    List.of(),
//                    EnchantCommonPlaceItemAction.TYPE
                    EnchantCommonSplitItemAction.TYPE
            ));
        });
    }

    // ----------------------------------------- Item Insert End -------------------------------------------------------------//



    // ----------------------------------------- Empty Use Start -------------------------------------------------------------//
    public void addEmptyUse(int time) {
        addEmptyUse(this, time);
    }

    public void addEmptyUse(BlockPos pos, int time) {
        addEmptyUse(this.steps, pos, time);
    }

    public static void addEmptyUse(CraftGuideOperator2 craftGuideOperator2, int time) {
        addEmptyUse(craftGuideOperator2.steps(), craftGuideOperator2.pos(), time);
    }

    public static void addEmptyUse(List<CraftGuideStepData> steps, BlockPos pos, int time) {
        for (int i = 0; i < time; i++) {
            addEmptyUse(steps, pos);
        }
    }


    public void addEmptyUse() {
        addEmptyUse(this);
    }

    public void addEmptyUse(BlockPos pos) {
        addEmptyUse(this.steps, pos);
    }

    public static void addEmptyUse(CraftGuideOperator2 craftGuideOperator2) {
        addEmptyUse(craftGuideOperator2.steps(), craftGuideOperator2.pos());
    }

    public static void addEmptyUse(List<CraftGuideStepData> steps, BlockPos pos) {
        addEmptyUse(steps, pos, List.of());
    }


    public void addEmptyUse(ItemStack result) {
        addEmptyUse(this, result);
    }

    public void addEmptyUse(BlockPos pos, ItemStack result) {
        addEmptyUse(this.steps, pos, result);
    }

    public static void addEmptyUse(CraftGuideOperator2 craftGuideOperator2, ItemStack result) {
        addEmptyUse(craftGuideOperator2.steps(), craftGuideOperator2.pos(), result);
    }

    public static void addEmptyUse(List<CraftGuideStepData> steps, BlockPos pos, ItemStack result) {
        addEmptyUse(steps, pos, List.of(result));
    }


    public void addEmptyUse(List<ItemStack> results) {
        addEmptyUse(this, results);
    }

    public void addEmptyUse(BlockPos pos, List<ItemStack> results) {
        addEmptyUse(this.steps, pos, results);
    }

    public static void addEmptyUse(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> results) {
        addEmptyUse(craftGuideOperator2.steps(), craftGuideOperator2.pos(), results);
    }

    /**
     * 添加一个的使用步骤到工艺指南步骤列表中
     *
     * @param steps 工艺指南步骤数据列表，用于存储所有的工艺步骤
     * @param pos 方块位置，表示该步骤对应的目标方块位置
     * @param results 物品堆列表，表示该步骤产生的结果物品
     */
    public static void addEmptyUse(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> results) {
        steps.add(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                results,
                EnchantCommonUseAction.TYPE
        ));
    }

    // ----------------------------------------- Empty Use End -------------------------------------------------------------//


    public void addEmptyUseIfFail(ItemStack result, CraftGuideStepData... failSteps) {
        ItemUseStepUtil.addStepIfFail(this.steps, this.pos, List.of(), List.of(result), failSteps);
    }

    public void addEmptyUseIfFail(List<ItemStack> results, CraftGuideStepData... failSteps) {
        ItemUseStepUtil.addStepIfFail(this.steps, this.pos, List.of(), results, failSteps);
    }

    public void addEmptyUseIfFail(BlockPos pos, ItemStack result, CraftGuideStepData... failSteps) {
        ItemUseStepUtil.addStepIfFail(this.steps, pos, List.of(), List.of(result), failSteps);
    }

    public void addEmptyUseIfFail(BlockPos pos, List<ItemStack> results, CraftGuideStepData... failSteps) {
        ItemUseStepUtil.addStepIfFail(this.steps, pos, List.of(), results, failSteps);
    }

    // ----------------------------------------- Item Use Start -------------------------------------------------------------//
    public void addItemUse(ItemStack item, ItemStack result) {
        addItemUse(this, item, result);
    }

    public void addItemUseIfFail(ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        addItemUseIfFail(item, List.of(result), failSteps);
    }

    public void addItemUse(BlockPos pos, ItemStack item, ItemStack result) {
        addItemUse(this.steps, pos, item, result);
    }

    public static void addItemUse(CraftGuideOperator2 craftGuideOperator2, ItemStack item, ItemStack result) {
        addItemUse(craftGuideOperator2.steps, craftGuideOperator2.pos(), item, result);
    }

    public static void addItemUse(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack result) {
        addItemUse(steps, pos, item, List.of(result));
    }


    public void addItemUse(ItemStack item, List<ItemStack> results) {
        addItemUse(this, item, results);
    }

    public void addItemUseIfFail(ItemStack item, List<ItemStack> results, CraftGuideStepData... failSteps) {
        ItemUseStepUtil.addStepIfFail(this.steps, this.pos, List.of(item), results, failSteps);
    }

    public void addItemUse(BlockPos pos, ItemStack item, List<ItemStack> results) {
        addItemUse(this.steps, pos, item, results);
    }

    public static void addItemUse(CraftGuideOperator2 craftGuideOperator2, ItemStack item, List<ItemStack> results) {
        addItemUse(craftGuideOperator2.steps(), craftGuideOperator2.pos(), item, results);
    }

    /**
     * 添加物品使用步骤到工艺指南步骤列表中
     *
     * @param steps 工艺指南步骤列表，用于存储所有的工艺步骤数据
     * @param pos 方块位置，指定物品使用的位置
     * @param item 物品堆栈，表示要使用的物品
     * @param results 结果物品列表，表示使用该物品后可能产生的结果
     */
    public static void addItemUse(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, List<ItemStack> results) {
        // 创建新的工艺指南步骤数据并添加到步骤列表中
        steps.add(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(item),
                results,
                EnchantCommonUseAction.TYPE
        ));
    }



    public void addItemUse(List<ItemStack> items) {
        addItemUse(this, items);
    }

    public void addItemUse(BlockPos pos, List<ItemStack> items) {
        addItemUse(this.steps, pos, items);
    }

    public static void addItemUse(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items) {
        addItemUse(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items);
    }

    public static void addItemUse(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        for (ItemStack item : items) {
            addItemUse(steps, pos, item);
        }
    }


    public void addItemUse(ItemStack item) {
        addItemUse(this, item);
    }

    public void addItemUse(BlockPos pos, ItemStack item) {
        addItemUse(this.steps, pos, item);
    }

    public static void addItemUse(CraftGuideOperator2 craftGuideOperator2, ItemStack item) {
        addItemUse(craftGuideOperator2.steps(), craftGuideOperator2.pos(), item);
    }

    public static void addItemUse(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        steps.add(makeItemUseStep(pos, item));
    }

    public static void addItemUseIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, ItemStack... backItems) {
        addItemUseIfFail0(steps, pos, item, List.of(backItems));
    }

    public static void addItemUseIfFail0(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, List<ItemStack> backItems) {
        List<CraftGuideStepData> failSteps = backItems.stream()
                .map(itemStack -> makeItemUseStep(pos, itemStack))
                .toList();

        addItemUseIfFail(steps, pos, item, failSteps);
    }

    public static void addItemUseIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, CraftGuideStepData... failSteps) {
        addItemUseIfFail(steps, pos, item, List.of(failSteps));
    }

    public static void addItemUseIfFail(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item, List<CraftGuideStepData> failSteps) {
        steps.add(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(item),
                List.of(),
                EnchantCommonUseAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        ));
    }
    // ----------------------------------------- Item Use End -------------------------------------------------------------//



    // ----------------------------------------- Make Item Use Start -------------------------------------------------------------//
    // 必须层
    public static CraftGuideStepData makeItemUseStep(BlockPos pos, ItemStack item) {
        return makeItemUseStep(pos, List.of(item), List.of());
    }

    public static CraftGuideStepData makeItemUseStep(BlockPos pos, ItemStack item, ItemStack result) {
        return makeItemUseStep(pos, List.of(item), List.of(result));
    }

    public static CraftGuideStepData makeItemUseStep(BlockPos pos, List<ItemStack> items, ItemStack result) {
        return makeItemUseStep(pos, items, List.of(result));
    }

    public static CraftGuideStepData makeItemUseStep(BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        return new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE
        );
    }


    // 可选层
    public static CraftGuideStepData makeOptionalItemUseStep(BlockPos pos, ItemStack item) {
        return makeOptionalItemUseStep(pos, List.of(item), List.of());
    }

    public static CraftGuideStepData makeOptionalItemUseStep(BlockPos pos, ItemStack item, ItemStack result) {
        return makeOptionalItemUseStep(pos, List.of(item), List.of(result));
    }

    public static CraftGuideStepData makeOptionalItemUseStep(BlockPos pos, List<ItemStack> items, ItemStack result) {
        return makeOptionalItemUseStep(pos, items, List.of(result));
    }

    public static CraftGuideStepData makeOptionalItemUseStep(BlockPos pos, List<ItemStack> items, List<ItemStack> results) {
        return new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(ActionOption.OPTIONAL, true)
        );
    }


//    // 失败层
//    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, ItemStack item, CraftGuideStepData... failSteps) {
//        return makeItemUseStepIfFail(pos, List.of(item), List.of(), failSteps);
//    }
//
//    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
//        return makeItemUseStepIfFail(pos, List.of(item), List.of(result), failSteps);
//    }
//
//    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
//        return makeItemUseStepIfFail(pos, items, List.of(result), failSteps);
//    }
//
//    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<ItemStack> backItems) {
//        backItems.stream()
//                .map(itemStack -> makeItemUseStep(pos, itemStack))
//
//        return makeItemUseStepIfFail(pos, items, results, List.of(failSteps));
//    }


    // 失败层
    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, ItemStack item, CraftGuideStepData... failSteps) {
        return makeItemUseStepIfFail(pos, List.of(item), List.of(), failSteps);
    }

    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, ItemStack item, ItemStack result, CraftGuideStepData... failSteps) {
        return makeItemUseStepIfFail(pos, List.of(item), List.of(result), failSteps);
    }

    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, List<ItemStack> items, ItemStack result, CraftGuideStepData... failSteps) {
        return makeItemUseStepIfFail(pos, items, List.of(result), failSteps);
    }

    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, List<ItemStack> items, List<ItemStack> results, CraftGuideStepData... failSteps) {
        return makeItemUseStepIfFail(pos, items, results, List.of(failSteps));
    }


    // 失败层
    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, ItemStack item, List<CraftGuideStepData> failSteps) {
        return makeItemUseStepIfFail(pos, List.of(item), List.of(), failSteps);
    }

    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, ItemStack item, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeItemUseStepIfFail(pos, List.of(item), List.of(result), failSteps);
    }

    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, List<ItemStack> items, ItemStack result, List<CraftGuideStepData> failSteps) {
        return makeItemUseStepIfFail(pos, items, List.of(result), failSteps);
    }

    public static CraftGuideStepData makeItemUseStepIfFail(BlockPos pos, List<ItemStack> items, List<ItemStack> results, List<CraftGuideStepData> failSteps) {
        return new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                items,
                results,
                EnchantCommonUseAction.TYPE,
                FailCraftGuideStepData.toCompoundTag(failSteps)
        );
    }

    // ----------------------------------------- Make Item Use End -------------------------------------------------------------//



    // ----------------------------------------- Tool Use Start -------------------------------------------------------------//
    public void addToolUse(ItemStack tool) {
        addToolUse(this, tool);
    }

    public void addToolUse(BlockPos pos, ItemStack tool) {
        addToolUse(this.steps, pos, tool);
    }

    public static void addToolUse(CraftGuideOperator2 craftGuideOperator2, ItemStack tool) {
        addToolUse(craftGuideOperator2.steps(), craftGuideOperator2.pos(), tool);
    }

    public static void addToolUse(List<CraftGuideStepData> steps, BlockPos pos, ItemStack tool) {
        steps.add(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(tool),
                List.of(tool),
                EnchantCommonUseAction.TYPE
        ));
    }
    // ----------------------------------------- Tool Use End -------------------------------------------------------------//



    // ----------------------------------------- Idle Start -------------------------------------------------------------//
    public void addIdle() {
        IdleStepUtil.addStep(this);
    }

    public void addIdle(BlockPos pos) {
        IdleStepUtil.addStep(this.steps, pos);
    }

    public void addIdle(int tickTime) {
        IdleStepUtil.addStep(this, tickTime);
    }

    public void addIdle(BlockPos pos, int tickTime) {
        IdleStepUtil.addStep(this.steps, pos, tickTime);
    }


    public void addIdleLimit() {
        IdleStepUtil.addStepWithLimit(this);
    }

    public void addIdleLimit(int tickTime) {
        IdleStepUtil.addStepWithLimit(this, tickTime);
    }

    public void addIdleLimit(BlockPos pos) {
        IdleStepUtil.addStepWithLimit(this.steps, pos);
    }
    public void addIdleLimit(BlockPos pos, int tickTime) {
        IdleStepUtil.addStepWithLimit(this.steps, pos, tickTime);
    }

    // ----------------------------------------- Idle End -------------------------------------------------------------//



    // ----------------------------------------- Item Pickup Start -------------------------------------------------------------//
    public void addItemPickup(ItemStack item) {
        addItemPickup(this, item);
    }

    public void addItemPickup(BlockPos pos, ItemStack item) {
        addItemPickup(this.steps, pos, item);
    }

    public static void addItemPickup(CraftGuideOperator2 craftGuideOperator2, ItemStack item) {
        addItemPickup(craftGuideOperator2.steps(), craftGuideOperator2.pos(), item);
    }

    public static void addItemPickup(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        addItemPickup(steps, pos, List.of(item));
    }


    public void addItemPickupOptional(ItemStack item) {
        addItemPickupOptional(this, item);
    }

    public void addItemPickupOptional(BlockPos pos, ItemStack item) {
        addItemPickupOptional(this.steps, pos, item);
    }

    public static void addItemPickupOptional(CraftGuideOperator2 craftGuideOperator2, ItemStack item) {
        addItemPickupOptional(craftGuideOperator2.steps(), craftGuideOperator2.pos(), item);
    }

    public static void addItemPickupOptional(List<CraftGuideStepData> steps, BlockPos pos, ItemStack item) {
        addItemPickupOptional(steps, pos, List.of(item));
    }


    public void addItemPickup(List<ItemStack> items) {
        addItemPickup(this, items);
    }


    public void addItemPickupIfFail(List<ItemStack> items, CraftGuideStepData... failSteps) {
        ItemPickupUtil.addStepIfFail(this.steps, pos, items, failSteps);
    }

    public void addItemPickup(BlockPos pos, List<ItemStack> items) {
        addItemPickup(this.steps, pos, items);
    }

    public void addItemPickupIfFail(BlockPos pos, List<ItemStack> items, CraftGuideStepData... failSteps) {
        ItemPickupUtil.addStepIfFail(this.steps, pos, items, failSteps);
    }

    public static void addItemPickup(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items) {
        addItemPickup(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items);
    }

    public static void addItemPickup(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        steps.add(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                items,
                EnchantCommonPickupItemAction.TYPE
        ));
    }


    public void addItemPickupOptional(List<ItemStack> items) {
        addItemPickupOptional(this, items);
    }

    public void addItemPickupOptional(BlockPos pos, List<ItemStack> items) {
        addItemPickupOptional(this.steps, pos, items);
    }

    public static void addItemPickupOptional(CraftGuideOperator2 craftGuideOperator2, List<ItemStack> items) {
        addItemPickupOptional(craftGuideOperator2.steps(), craftGuideOperator2.pos(), items);
    }

    public static void addItemPickupOptional(List<CraftGuideStepData> steps, BlockPos pos, List<ItemStack> items) {
        steps.add(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                items,
                EnchantCommonPickupItemAction.TYPE,
                ActionOptionSet.with(ActionOption.OPTIONAL, true)
        ));
    }
    // ----------------------------------------- Item Pickup End -------------------------------------------------------------//


    // ----------------------------------------- Sneak Item Use Start -------------------------------------------------------------//
    public void addSneakUse(ItemStack input, ItemStack output) {
        ItemUseStepUtil.addStep(this, input, output);
    }

    public void addSneakUse(ItemStack input, List<ItemStack> outputs) {
        ItemUseStepUtil.addStep(this.steps, this.pos, List.of(input), outputs);
    }

    public void addSneakUse(List<ItemStack> inputs, List<ItemStack> outputs) {
        ItemUseStepUtil.addStep(this.steps, this.pos, inputs, outputs);
    }

    public void addSneakUse(List<ItemStack> inputs, ItemStack output) {
        ItemUseStepUtil.addStep(this.steps, this.pos, inputs, List.of(output));
    }
    // ----------------------------------------- Sneak Item Use Start -------------------------------------------------------------//



    // ----------------------------------------- Sneak Tool Use Start -------------------------------------------------------------//
    public void addSneakToolUse(ItemStack tool, ItemStack output) {
        ItemUseStepUtil.addStep(this.steps, this.pos, List.of(tool), List.of(tool, output));
    }

    public void addSneakToolUseIfFail(ItemStack tool, ItemStack output, CraftGuideStepData... failSteps) {
        ItemUseStepUtil.addStepIfFail(this.steps, this.pos, List.of(tool), List.of(tool, output), failSteps);
    }

    public void addSneakToolUse(ItemStack tool, List<ItemStack> outputs) {
        List<ItemStack> allOutputs = Lists.newArrayList(tool);
        allOutputs.addAll(outputs);
        ItemUseStepUtil.addStep(this.steps, this.pos, List.of(tool), allOutputs);
    }

    public void addSneakToolUseIfFail(ItemStack tool, List<ItemStack> outputs, CraftGuideStepData... failSteps) {
        List<ItemStack> allOutputs = Lists.newArrayList(tool);
        allOutputs.addAll(outputs);
        ItemUseStepUtil.addStepWithSneakIfFail(this.steps, this.pos, List.of(tool), allOutputs, failSteps);
    }
    // ----------------------------------------- Sneak Tool Use Start -------------------------------------------------------------//



    public Target makeTargetNoSide(ResourceLocation id) {
        return TargetUtil.makeTargetNoSide(id, pos);
    }

    public Target makeTarget(ResourceLocation id, @Nullable Direction side) {
        return TargetUtil.makeTarget(id, pos, side);
    }


    public Target makeTargetVirtualNoSide() {
        return TargetUtil.makeTargetVirtualNoSide(pos);
    }

    public Target makeTargetVirtual(ResourceLocation id, @Nullable Direction side) {
        return TargetUtil.makeTargetVirtual(id, pos, side);
    }




    public static void forEachSingleItem(List<ItemStack> items, Consumer<ItemStack> consumer) {
        for (ItemStack itemStack : items) {
            for (int i = 0; i < itemStack.getCount(); i++) {
                consumer.accept(itemStack.copyWithCount(1));
            }
        }
    }
    public static void forEachSingleItem(ItemStack itemStack, Consumer<ItemStack> consumer) {
        for (int i = 0; i < itemStack.getCount(); i++) {
            consumer.accept(itemStack.copyWithCount(1));
        }
    }

    public static void forEachItem(List<ItemStack> items, Consumer<ItemStack> consumer) {
        for (ItemStack itemStack : items) {
            consumer.accept(itemStack.copyWithCount(1));
        }
    }

    public static void forEach2Items(List<ItemStack> input, Consumer<List<ItemStack>> consumer) {
        forEachItems(2, input, consumer);
    }

    public static void forEach3Items(List<ItemStack> input, Consumer<List<ItemStack>> consumer) {
        forEachItems(3, input, consumer);
    }

    public static void forEachItems(int col, List<ItemStack> input, Consumer<List<ItemStack>> consumer) {
        for (int i = 0; i < input.size(); i += col) {
            int end = Math.min(i + col, input.size());
            List<ItemStack> toDeal = input.subList(i, end);
            consumer.accept(toDeal);
        }
    }

    public static <T> List<T> mapEach2Items(List<ItemStack> input, Function<List<ItemStack>, T> consumer) {
        return mapEachItems(2, input, consumer);
    }

    public static <T> List<T> mapEach3Items(List<ItemStack> input, Function<List<ItemStack>, T> consumer) {
        return mapEachItems(3, input, consumer);
    }

    public static <T> List<T> mapEachItems(int col, List<ItemStack> input, Function<List<ItemStack>, T> consumer) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < input.size(); i += col) {
            int end = Math.min(i + col, input.size());
            List<ItemStack> toDeal = input.subList(i, end);
            result.add(consumer.apply(toDeal));
        }
        return result;
    }
}
