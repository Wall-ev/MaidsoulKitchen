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

public class WaterCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final WaterCookRule INSTANCE = new WaterCookRule<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> WaterCookRule<B, R> getInstance() {
        return (WaterCookRule<B, R>) INSTANCE;
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

        boolean hasEnoughFluid = cookBeBase.hasFluid();
        boolean recMatch = cookBeBase.recMatch();
        List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
        // 厨具满足烹饪的外部条件和有符合配方的原材料
        boolean hasFuel = cm.hasItem(itemStack -> {
            return ItemStackUtil.isItem(activeItemStacks, itemStack);
        });
        if ((hasEnoughFluid || hasFuel) && !recMatch) {
            boolean hasMaidRecs = cm.hasMaidRecs(cookBeBase);
            if (hasMaidRecs) {
                return true;
            }
        }

        if (recMatch && !hasEnoughFluid && hasFuel && hasInputAvailableSlot) {
            return true;
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

        boolean canTakeResult = cookBeBase.canTakeResult();
        ItemStack result = cookBeBase.getResult();
        // 取出成品
        if (canTakeResult && !result.isEmpty()) {
            cookBeBase.takeItem(result, outputInv);
            cookBeBase.awardExp();
            cookBeBase.markChanged();
        }


        boolean hasEnoughFluid = cookBeBase.hasFluid();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残存的原材料
        if (!recMatch && hasInputs) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();
        }

        List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
        // 厨具满足烹饪的外部条件和有符合配方的原材料
        ItemStack fuel = cm.getItem(itemStack -> {
            return ItemStackUtil.isItem(activeItemStacks, itemStack);
        });

        // 放入烹饪的原材料
        if ((hasEnoughFluid || !fuel.isEmpty()) && !recMatch && cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            cookBeBase.insertInputs(cm.pollMaidRec(cookBeBase), itemInventory);
            cookBeBase.markChanged();
            cm.getItemInventory().markDirty();;
            recMatch = true;
        }

        // 补充水分
        if (recMatch && !hasEnoughFluid && !fuel.isEmpty()) {
            cookBeBase.useItem(fuel, () -> {
                return cookBeBase.hasFluid();
            }, inputInv);
            cookBeBase.markChanged();
        }
    }

}
