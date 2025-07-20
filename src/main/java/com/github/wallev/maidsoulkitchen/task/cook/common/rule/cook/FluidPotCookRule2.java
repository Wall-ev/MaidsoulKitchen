package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.FluidRecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

// 代码检测完成
// 已测试：2025年7月19日15:46:47
/**
 * 妖归发酵桶
 */
public class FluidPotCookRule2<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final FluidPotCookRule2 INSTANCE = new FluidPotCookRule2<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> FluidPotCookRule2<B, R> getInstance() {
        return (FluidPotCookRule2<B, R>) INSTANCE;
    }

    public boolean canMoveTo(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        IMaidCookInventory cookInv = cm.getCookInv();
        boolean hasInputAvailableSlot = cookInv.hasInputAvailableSlot();
        boolean hasOutputAvailableSlot = cookInv.hasOutputAvailableSlot();

        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        boolean hasFluid = cookBeBase.hasFluid();

        // 取出(有条件取出)成品: 厨具可以取出成品 && 有成品 && 烹饪中枢或者绑定的输入容器内存在对应的餐具 && 烹饪中枢有空的输出槽位
        if (!recMatch && hasFluid && hasOutputAvailableSlot) {
            if (hasFluidContainers(cookBeBase.getFluid(), cm)) {
                return true;
            }
        }

        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 有符合配方的原材料
        if (matchCookState && !hasInputs && !recMatch && !hasFluid && cm.hasMaidRecs(cookBeBase)) {
            return true;
        }

        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (hasInputs && hasInputAvailableSlot && !recMatch) {
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

        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        FluidStack fluidStack = cookBeBase.getFluidStack();

        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (!recMatch && hasInputs && hasInputAvailableSlot) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();
        }

        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 有符合配方的原材料
        if (matchCookState && !recMatch && fluidStack.isEmpty() && !hasInputs && cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            MaidRec maidRec = cm.pollMaidRec(cookBeBase);
            cookBeBase.insertFluidItems(maidRec.fluidItem(), itemInventory, inputInv);
            cookBeBase.insertInputs(maidRec, itemInventory);
            cookBeBase.markChanged();
            recMatch = true;
        }

        // 取出(有条件取出)成品: 厨具可以取出成品 && 有成品 && 烹饪中枢或者绑定的输入容器内存在对应的餐具 && 烹饪中枢有空的输出槽位
        if (!recMatch && !fluidStack.isEmpty() && hasOutputAvailableSlot) {
            Fluid fluid = fluidStack.getFluid();
            ItemStack fluidContainer = getFluidContainers(fluid, cm);
            cookBeBase.useItem(fluidContainer, () -> {
                return !fluidStack.isEmpty();
            }, outputInv);
        }
    }

}
