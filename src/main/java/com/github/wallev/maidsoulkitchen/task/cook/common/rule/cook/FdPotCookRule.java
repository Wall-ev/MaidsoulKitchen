package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.util.MaidUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FdPotCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final FdPotCookRule INSTANCE = new FdPotCookRule<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> FdPotCookRule<B, R> getInstance() {
        return (FdPotCookRule<B, R>) INSTANCE;
    }

    public boolean canMoveTo(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        boolean canTakeResult = cookBeBase.canTakeResult();
        boolean hasResult = cookBeBase.hasResult();
        // 有成品
        if (canTakeResult && hasResult) {
            return true;
        }

        boolean hasMeal = cookBeBase.hasMeal();
        // 有待取出成品(有条件取出)和对应的餐具
        if (hasMeal) {
            ItemStack needContainer = cookBeBase.getNeedContainer();
            if (!needContainer.isEmpty() && rm.hasItemFromOutputAddition(needContainer)) {
                return true;
            }
        }

        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        // 厨具满足烹饪的外部条件和有符合配方的原材料
        if (matchCookState && !recMatch) {
            boolean hasMaidRecs = rm.hasMaidRecs(cookBeBase);
            if (hasMaidRecs) {
                return true;
            }
        }

        boolean hasInputs = cookBeBase.hasInputs();
        // 配方不存在以及有残留的物品
        if (!recMatch && hasInputs) {
            return true;
        }

        boolean hasContainer = cookBeBase.hasContainer();
        // 厨锅没有物品并且有餐具
        if (!hasInputs && hasContainer) {
            return true;
        }

        return false;
    }

    public void cookMake(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        boolean pickAction = false;

        IItemHandlerModifiable inputInv = rm.getInputInv();
        IItemHandlerModifiable outputInv = rm.getOutputInv();
        IItemHandlerModifiable outputAdditionInv = rm.getOutputAdditionInv();

        ItemStack meal = cookBeBase.getMeal();
        ItemStack nowContainer = cookBeBase.getNowContainer();
        // 放入餐具
        if (!meal.isEmpty()) {
            // 取出餐具（不匹配）
            if (!nowContainer.isEmpty()) {
                cookBeBase.takeItem(nowContainer, outputAdditionInv);
            }

            ItemStack needContainer = cookBeBase.getNeedContainer();
            ItemStack outputAdditionItem = rm.getItemFromOutputAddition(needContainer);
            // 放入餐具
            cookBeBase.insertContainer(outputAdditionItem);
            cookBeBase.markChanged();

            pickAction = true;
        }

        boolean canTakeResult = cookBeBase.canTakeResult();
        ItemStack result = cookBeBase.getResult();
        // 取出成品
        if (canTakeResult && !result.isEmpty()) {
            cookBeBase.takeItem(result, outputInv);
            cookBeBase.awardExp();
            cookBeBase.markChanged();

            pickAction = true;
        }


        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残存的原材料
        if (!matchCookState && !recMatch && hasInputs) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();

            pickAction = true;
        }

        // 取出餐具
        if (!matchCookState && !recMatch && !nowContainer.isEmpty()) {
            cookBeBase.takeItem(nowContainer, outputAdditionInv);
            cookBeBase.markChanged();

            pickAction = true;
        }

        // 放入烹饪的原材料
        if (matchCookState && !recMatch && rm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = rm.getItemInventory();
            cookBeBase.insertInputs(rm.pollMaidRec(cookBeBase), itemInventory);
            cookBeBase.markChanged();
            rm.getItemInventory().markDirty();;

            pickAction = true;
        }

        if (pickAction) {
            MaidUtil.pickupAction(cookBeBase);
        }

    }

    @Override
    public FdPotCookRule<B, R> getOrCreate() {
        return this;
    }
}
