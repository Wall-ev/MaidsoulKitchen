package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.GatherResult;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.util.MaidUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

// 代码检测完成
// 已测试：2025年7月19日15:36:11

/**
 * 农夫乐事厨锅
 */
public class FdPotCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final FdPotCookRule INSTANCE = new FdPotCookRule<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> FdPotCookRule<B, R> getInstance() {
        return (FdPotCookRule<B, R>) INSTANCE;
    }

    @Override
    public boolean canMoveTo(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        IMaidCookInventory cookInv = cm.getCookInv();
        boolean hasInputAvailableSlot = cookInv.hasInputAvailableSlot();
        boolean hasOutputAvailableSlot = cookInv.hasOutputAvailableSlot();

        boolean canTakeResult = cookBeBase.canTakeResult();
        boolean hasResult = cookBeBase.hasResult();
        // 取出成品: 厨具可以取出成品 && 有成品 && 烹饪中枢有空的输出槽位
        if (canTakeResult && hasResult && hasOutputAvailableSlot) {
            return true;
        }

        boolean hasMeal = cookBeBase.hasMeal();
        ItemStack nowContainer = cookBeBase.getNowContainer();
        boolean hasContainer = !nowContainer.isEmpty();
        ItemStack needContainer = cookBeBase.getNeedContainer();
        // 取出有条件取出的成品:
        // 厨具有有条件取出的成品（需要对应的餐具） && 烹饪中枢有空的输出槽位 &&
        // 1. 厨具内存在餐具（即不符合对应的条件） && 烹饪中枢有空的输入槽位 && 烹饪中枢或者绑定的输入容器内存在对应的餐具
        // 2. 厨具内不存在餐具 && 绑定的输入容器内存在对应的餐具
        if (hasMeal && hasOutputAvailableSlot) {
            if (hasContainer) {
                if (hasInputAvailableSlot && !needContainer.isEmpty() && cm.hasItem(needContainer)) {
                    return true;
                }
            } else {
                if (!needContainer.isEmpty() && cm.hasItem(needContainer)) {
                    return true;
                }
            }
        }

        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (hasInputs && hasInputAvailableSlot && !recMatch) {
            return true;
        }

        // 取出残存的餐具: 厨具内有餐具 && 烹饪中枢有空的输入槽位 && 厨具内不存在物品
        if (hasContainer && hasInputAvailableSlot && !hasInputs) {
            return true;
        }

        boolean matchCookState = cookBeBase.cookStateMatch();
        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 有符合配方的原材料
        if (matchCookState && !recMatch && !hasMeal && !hasInputs && cm.hasMaidRecs(cookBeBase)) {
            return true;
        }

        return false;
    }

    @Override
    public void cookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        boolean pickAction = false;

        IItemHandlerModifiable inputInv = cm.getInputInv();
        IItemHandlerModifiable outputInv = cm.getOutputInv();

        IMaidCookInventory cookInv = cm.getCookInv();
        boolean hasInputAvailableSlot = cookInv.hasInputAvailableSlot();
        boolean hasOutputAvailableSlot = cookInv.hasOutputAvailableSlot();

        ItemStack meal = cookBeBase.getMeal();
        ItemStack nowContainer = cookBeBase.getNowContainer();
        // 放入餐具: 有待取出成品 &&
        // 1. 厨具内存在餐具（即不符合对应的条件） && 烹饪中枢有空的输入槽位 && 烹饪中枢或者绑定的输入容器内存在对应的餐具
        // 2. 厨具内不存在餐具 && 烹饪中枢或者绑定的输入容器内存在对应的餐具
        if (!meal.isEmpty()) {
            // 取出餐具（不匹配）
            if (!nowContainer.isEmpty()) {
                cookBeBase.takeItem(nowContainer, inputInv);
                pickAction = true;
            }

            ItemStack needContainer = cookBeBase.getNeedContainer();
            GatherResult gatherResult = cm.getItem(needContainer);
            if (!gatherResult.isFail()) {
                // 放入餐具
                ItemStack queryItemStack = gatherResult.queryItemStack(64);
                cookBeBase.insertContainer(queryItemStack);
                cookBeBase.markChanged();

                gatherResult.backItemStack(queryItemStack);

                pickAction = true;
            }
        }

        boolean canTakeResult = cookBeBase.canTakeResult();
        ItemStack result = cookBeBase.getResult();
        // 取出成品: 厨具可以取出成品 && 有成品 && 烹饪中枢有空的输出槽位
        if (canTakeResult && !result.isEmpty() && hasOutputAvailableSlot) {
            boolean taken = cookBeBase.takeItem(result, outputInv);
            cookBeBase.awardExp();
            cookBeBase.markChanged();

            if (taken) {
                pickAction = true;
            }
        }


        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (hasInputs && hasInputAvailableSlot && !recMatch) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();

            pickAction = true;
        }

        // 取出残存的餐具: 厨具内有餐具 && 烹饪中枢有空的输入槽位 && 厨具内不存在物品
        if (!nowContainer.isEmpty() && hasInputAvailableSlot && !hasInputs) {
            cookBeBase.takeItem(nowContainer, inputInv);
            cookBeBase.markChanged();

            pickAction = true;
        }

        boolean matchCookState = cookBeBase.cookStateMatch();
        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 有符合配方的原材料
        if (matchCookState && !hasInputs && !recMatch && meal.isEmpty() && cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            cookBeBase.insertInputs(cm.pollMaidRec(cookBeBase), itemInventory);
            cookBeBase.markChanged();
            cm.getItemInventory().markDirty();;

            pickAction = true;
        }

        if (pickAction) {
            MaidUtil.pickupAction(cookBeBase);
        }

    }

}
