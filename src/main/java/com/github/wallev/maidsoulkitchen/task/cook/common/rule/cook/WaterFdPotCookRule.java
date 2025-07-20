package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

// 代码检测完成
// 已测试：2025年7月19日16:21:39

/**
 * 妖归茶壶
 */
public class WaterFdPotCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final WaterFdPotCookRule INSTANCE = new WaterFdPotCookRule<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> WaterFdPotCookRule<B, R> getInstance() {
        return (WaterFdPotCookRule<B, R>) INSTANCE;
    }

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

        boolean cookStateMatch = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasEnoughFluid = cookBeBase.hasFluid();
        boolean hasInputs = cookBeBase.hasInputs();
        boolean findFuel = false;

        // 置入烹饪的原材料: (厨具满足烹饪的外部条件 || 烹饪中枢或者绑定的输入容器内存在对应的燃料) && 厨具内没有物品 && 有符合配方的原材料
        if (!recMatch && !hasInputs && !hasMeal && cm.hasMaidRecs(cookBeBase) && cookStateMatch) {
            if (!hasEnoughFluid) {
                List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
                findFuel = cm.hasItem(itemStack -> {
                    return ItemStackUtil.isItem(activeItemStacks, itemStack);
                });

                if (findFuel) {
                    return true;
                }
            } else {
                return true;
            }

            return true;
        }

        // 补充燃料: 厨具内有符合配方的原料 && 厨具满足外部烹饪条件 && 烹饪中枢或者绑定的输入容器内存在对应的燃料
        if (recMatch && cookStateMatch && !hasEnoughFluid) {
            if (!findFuel) {
                List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
                findFuel = cm.hasItem(itemStack -> {
                    return ItemStackUtil.isItem(activeItemStacks, itemStack);
                });
            }

            if (findFuel) {
                return true;
            }
        }

        // 取出残存的餐具: 厨具内有餐具 && 烹饪中枢有空的输入槽位 && 厨具内不存在物品
        if (hasContainer && hasInputAvailableSlot && !hasInputs) {
            return true;
        }

        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (hasInputs && hasInputAvailableSlot && !recMatch) {
            return true;
        }

        // 取出燃料：存在燃料 && 厨具内的物品不符合任一配方的材料 && 烹饪中枢有空的输入槽位
        if (hasEnoughFluid && cookStateMatch && !recMatch && hasInputAvailableSlot) {
            return true;
        }

        return false;
    }

    public void cookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
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
            }

            ItemStack needContainer = cookBeBase.getNeedContainer();
            ItemStack outputAdditionItem = cm.getItem(needContainer);
            if (!outputAdditionItem.isEmpty()) {
                // 放入餐具
                cookBeBase.insertContainer(outputAdditionItem);
                cookBeBase.markChanged();
            }
        }

        boolean canTakeResult = cookBeBase.canTakeResult();
        ItemStack result = cookBeBase.getResult();
        // 取出成品: 厨具可以取出成品 && 有成品 && 烹饪中枢有空的输出槽位
        if (canTakeResult && !result.isEmpty() && hasOutputAvailableSlot) {
            cookBeBase.takeItem(result, outputInv);
            cookBeBase.awardExp();
            cookBeBase.markChanged();
        }

        boolean cookStateMatch = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasEnoughFluid = cookBeBase.hasFluid();
        boolean hasInputs = cookBeBase.hasInputs();
        ItemStack findFluid = ItemStack.EMPTY;

        // 置入烹饪的原材料: (厨具满足烹饪的外部条件 || 烹饪中枢或者绑定的输入容器内存在对应的燃料) && 厨具内没有物品 && 有符合配方的原材料
        if (!recMatch && !hasInputs && meal.isEmpty() && cm.hasMaidRecs(cookBeBase)) {
            if (!cookStateMatch) {
                List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
                findFluid = cm.getItem(itemStack -> {
                    return ItemStackUtil.isItem(activeItemStacks, itemStack);
                });
                if (!findFluid.isEmpty()) {
                    ItemInventory itemInventory = cm.getItemInventory();
                    cookBeBase.insertInputs(cm.pollMaidRec(cookBeBase), itemInventory);
                    cookBeBase.markChanged();

                    recMatch = true;
                }
            } else {
                ItemInventory itemInventory = cm.getItemInventory();
                cookBeBase.insertInputs(cm.pollMaidRec(cookBeBase), itemInventory);
                cookBeBase.markChanged();

                recMatch = true;
            }
        }

        // 补充流体: 厨具内有符合配方的原料 && 厨具满足外部烹饪条件 && 烹饪中枢或者绑定的输入容器内存在对应的燃料
        if (recMatch && cookStateMatch && !hasEnoughFluid) {
            if (findFluid.isEmpty()) {
                List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
                findFluid = cm.getItem(itemStack -> {
                    return ItemStackUtil.isItem(activeItemStacks, itemStack);
                });
            }

            if (!findFluid.isEmpty()) {
                cookBeBase.useItem(findFluid, () -> {
                    return !cookBeBase.hasFluid();
                }, inputInv);
                cookBeBase.markChanged();
            }
        }

        // 取出残存的餐具: 厨具内有餐具 && 烹饪中枢有空的输入槽位 && 厨具内不存在物品
        if (!nowContainer.isEmpty() && hasInputAvailableSlot && !hasInputs) {
            cookBeBase.takeItem(nowContainer, inputInv);
            cookBeBase.markChanged();
        }

        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (hasInputs && hasInputAvailableSlot && !recMatch) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();
        }

//        // 取出流体：存在燃料 && 厨具内的物品不符合任一配方的材料 && 烹饪中枢有空的输入槽位
//        if (hasEnoughFluid && !recMatch && hasInputAvailableSlot) {
//            cookBeBase.takeItem(activeItemStack, inputInv);
//            cookBeBase.markChanged();
//        }
    }

}
