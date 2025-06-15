package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class FuelCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final FuelCookRule INSTANCE = new FuelCookRule<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> FuelCookRule<B, R> getInstance() {
        return (FuelCookRule<B, R>) INSTANCE;
    }

    public boolean canMoveTo(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        boolean canTakeResult = cookBeBase.canTakeResult();
        boolean hasResult = cookBeBase.hasResult();
        // 有成品
        if (canTakeResult && hasResult) {
            return true;
        }

        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
        // 厨具满足烹饪的外部条件和有符合配方的原材料
        boolean hasFuel = rm.hasItem(BagType.START_ADDITION, itemStack -> {
            return ItemStackUtil.isItem(activeItemStacks, itemStack);
        });
        if ((matchCookState || hasFuel) && !recMatch) {
            boolean hasMaidRecs = rm.hasMaidRecs(cookBeBase);
            if (hasMaidRecs) {
                return true;
            }
        }

        if (recMatch && !matchCookState && hasFuel) {
            return true;
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

        // 厨锅没有物品并且有燃料
        if (!hasInputs && !cookBeBase.activeItemStack().isEmpty()) {
            return true;
        }

        return false;
    }

    public void cookMake(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        IItemHandlerModifiable inputInv = rm.getInputInv();
        IItemHandlerModifiable outputInv = rm.getOutputInv();

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

        List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
        // 厨具满足烹饪的外部条件和有符合配方的原材料
        ItemStack fuel = rm.getItem(BagType.START_ADDITION, itemStack -> {
            return ItemStackUtil.isItem(activeItemStacks, itemStack);
        });
        if (recMatch && !matchCookState && !fuel.isEmpty()) {
            cookBeBase.insertItem(fuel, cookBeBase.activeItemInv(), cookBeBase.activeItemSlot());
            cookBeBase.markChanged();
        }

        // 放入烹饪的原材料
        if ((matchCookState || !fuel.isEmpty()) && !recMatch && rm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = rm.getItemInventory();
            cookBeBase.insertInputs(rm.pollMaidRec(cookBeBase), itemInventory);
            cookBeBase.markChanged();
            rm.getItemInventory().markDirty();;
        }

        // 厨锅没有物品并且有燃料
        ItemStack activeItemStack = cookBeBase.activeItemStack();
        if (!hasInputs && !activeItemStack.isEmpty()) {
            cookBeBase.takeItem(activeItemStack, inputInv);
            cookBeBase.markChanged();
        }
    }

    @Override
    public FuelCookRule<B, R> getOrCreate() {
        return this;
    }
}
