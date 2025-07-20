package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
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

public class FluidPotCookRule1Copy<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final FluidPotCookRule1Copy INSTANCE = new FluidPotCookRule1Copy<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> FluidPotCookRule1Copy<B, R> getInstance() {
        return (FluidPotCookRule1Copy<B, R>) INSTANCE;
    }

    public boolean canMoveTo(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        IMaidCookInventory cookInv = cm.getCookInv();
        boolean hasInputAvailableSlot = cookInv.hasInputAvailableSlot();
        boolean hasOutputAvailableSlot = cookInv.hasOutputAvailableSlot();

        boolean canTakeResult = cookBeBase.canTakeResult();
        boolean hasResult = cookBeBase.hasResult();
        // 有成品
        if (canTakeResult && hasResult && hasOutputAvailableSlot) {
            return true;
        }
        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();

        boolean hasFluid = cookBeBase.hasFluid();
        // 有待取出成品(有条件取出)和对应的餐具
        if (!recMatch && hasFluid && hasInputAvailableSlot) {
            if (hasFluidContainers(cookBeBase.getFluid(), cm)) {
                return true;
            }
        }

        // 厨具满足烹饪的外部条件和有符合配方的原材料
        if (matchCookState && !recMatch) {
            boolean hasMaidRecs = cm.hasMaidRecs(cookBeBase);
            if (hasMaidRecs) {
                return true;
            }
        }

        boolean hasInputs = cookBeBase.hasInputs();
        // 配方不存在以及有残留的物品
        if (!recMatch && hasInputs && hasInputAvailableSlot) {
            return true;
        }

        boolean hasContainer = cookBeBase.hasContainer();
        // 厨锅没有物品并且有餐具
        if (!hasInputs && hasContainer && hasInputAvailableSlot) {
            return true;
        }

        return false;
    }

    public void cookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        IItemHandlerModifiable inputInv = cm.getInputInv();
        IItemHandlerModifiable outputInv = cm.getOutputInv();

        ItemStack meal = cookBeBase.getMeal();
        ItemStack nowContainer = cookBeBase.getNowContainer();
        // 放入餐具
        if (!meal.isEmpty()) {
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
        // 取出成品
        if (canTakeResult && !result.isEmpty()) {
            cookBeBase.takeItem(result, outputInv);
            cookBeBase.awardExp();
            cookBeBase.markChanged();
        }


        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残存的原材料
        if (!recMatch && hasInputs) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();
        }

        // 取出餐具
        if (!recMatch && !nowContainer.isEmpty()) {
            cookBeBase.takeItem(nowContainer, inputInv);
            cookBeBase.markChanged();
        }

        // 放入烹饪的原材料
        if (matchCookState && !recMatch && cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            MaidRec maidRec = cm.pollMaidRec(cookBeBase);
            cookBeBase.insertFluidItems(maidRec.fluidItem(), itemInventory, inputInv);
            cookBeBase.insertInputs(maidRec, itemInventory);
            cookBeBase.markChanged();
            cm.getItemInventory().markDirty();;
            recMatch = true;
        }

        FluidStack fluidStack = cookBeBase.getFluidStack();
        if (!recMatch && !fluidStack.isEmpty()) {
            Fluid fluid = fluidStack.getFluid();
            ItemStack fluidContainer = getFluidContainers(fluid, cm);
            cookBeBase.useItem(fluidContainer, () -> {
                return !fluidStack.isEmpty();
            }, outputInv);
        }
    }

}
