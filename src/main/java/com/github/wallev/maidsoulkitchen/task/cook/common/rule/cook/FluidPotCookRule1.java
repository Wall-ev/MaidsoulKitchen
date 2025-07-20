package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

// 代码检测完成
// 已测试：2025年7月19日15:36:19
/**
 * 饮酒作乐发酵桶
 */
public class FluidPotCookRule1<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final FluidPotCookRule1 INSTANCE = new FluidPotCookRule1<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> FluidPotCookRule1<B, R> getInstance() {
        return (FluidPotCookRule1<B, R>) INSTANCE;
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

        ItemStack nowContainer = cookBeBase.getNowContainer();
        boolean hasContainer = !nowContainer.isEmpty();

        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();

        boolean hasFluid = cookBeBase.hasFluid();
        // 取出有条件取出的成品:
        // 厨具有有条件取出的成品（需要对应的餐具） && 烹饪中枢有空的输出槽位 &&
        // 1. 厨具内存在餐具（即不符合对应的条件） && 烹饪中枢有空的输入槽位 && 烹饪中枢或者绑定的输入容器内存在对应的餐具
        // 2. 厨具内不存在餐具 && 绑定的输入容器内存在对应的餐具
        if (hasFluid && hasOutputAvailableSlot && !recMatch) {
            if (hasContainer) {
                if (hasInputAvailableSlot && hasFluidContainers(cookBeBase.getFluid(), cm)) {
                    return true;
                }
            } else {
                if (hasFluidContainers(cookBeBase.getFluid(), cm)) {
                    return true;
                }
            }
        }

        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (hasInputs && hasInputAvailableSlot && !recMatch) {
            return true;
        }

        // 取出残存的餐具: 厨具内有餐具 && 烹饪中枢有空的输入槽位 && 厨具内不存在物品
        if (hasContainer && hasInputAvailableSlot && !hasInputs) {
            return true;
        }

        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 厨具内不存在流体 && 有符合配方的原材料
        if (matchCookState && !recMatch && !hasInputs && !hasFluid && cm.hasMaidRecs(cookBeBase)) {
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
        if (!meal.isEmpty() && hasInputAvailableSlot) {
            // 取出餐具（不匹配）
            if (!nowContainer.isEmpty()) {
                cookBeBase.takeItem(nowContainer, inputInv);
            }

            ItemStack needContainer = cookBeBase.getNeedContainer();
            ItemStack outputAdditionItem = cm.getItem(needContainer);
            // 放入餐具
            cookBeBase.insertContainer(outputAdditionItem);
            cookBeBase.markChanged();
        }

        boolean canTakeResult = cookBeBase.canTakeResult();
        ItemStack result = cookBeBase.getResult();
        // 取出成品: 厨具可以取出成品 && 有成品 && 烹饪中枢有空的输出槽位
        if (canTakeResult && !result.isEmpty() && hasOutputAvailableSlot) {
            cookBeBase.takeItem(result, outputInv);
            cookBeBase.awardExp();
            cookBeBase.markChanged();
        }


        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (!recMatch && hasInputs && hasInputAvailableSlot) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();
        }

        // 取出残存的餐具: 厨具内有餐具 && 烹饪中枢有空的输入槽位 && 厨具内不存在物品
        if (!recMatch && !nowContainer.isEmpty() && hasInputAvailableSlot) {
            cookBeBase.takeItem(nowContainer, inputInv);
            cookBeBase.markChanged();
        }

        FluidStack fluidStack = cookBeBase.getFluidStack();
        boolean hasFluid = !fluidStack.isEmpty();

        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 有符合配方的原材料
        if (matchCookState && !recMatch && !hasInputs && !hasFluid && cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            MaidRec maidRec = cm.pollMaidRec(cookBeBase);
            cookBeBase.insertFluidItems(maidRec.fluidItem(), itemInventory, inputInv);
            cookBeBase.insertInputs(maidRec, itemInventory);
            cookBeBase.markChanged();
            recMatch = true;
        }

        // 取出有条件残存的物品2——流体: 有符合配方的流体 && 烹饪中枢有空的输出槽位
        if (!recMatch && hasFluid && hasOutputAvailableSlot) {
            Fluid fluid = fluidStack.getFluid();
            ItemStack fluidContainer = getFluidContainers(fluid, cm);
            cookBeBase.useItem(fluidContainer, () -> {
                return !fluidStack.isEmpty();
            }, outputInv);
        }
    }

}
