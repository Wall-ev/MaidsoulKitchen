package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.GatherResult;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

// 代码检测完成
// 待测试
/**
 * 胡萝卜厨房的发酵桶
 */
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
        // 取出成品: 厨具可以取出成品 && 有成品 && 烹饪中枢有空的输出槽位
        if (canTakeResult && hasResult && hasOutputAvailableSlot) {
            return true;
        }

        boolean cookStateMatch = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasEnoughFluid = cookBeBase.hasFluid();
        boolean hasInputs = cookBeBase.hasInputs();
        boolean findFuel = false;

        // 置入烹饪的原材料: (厨具满足烹饪的外部条件 || 烹饪中枢或者绑定的输入容器内存在对应的燃料) && 厨具内没有物品 && 有符合配方的原材料
        if (!recMatch && !hasInputs && cm.hasMaidRecs(cookBeBase) && cookStateMatch) {
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
        if (recMatch && !cookStateMatch && !hasEnoughFluid) {
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
        GatherResult findFluidResult = GatherResult.FAIL;

        // 置入烹饪的原材料: (厨具满足烹饪的外部条件 || 烹饪中枢或者绑定的输入容器内存在对应的燃料) && 厨具内没有物品 && 有符合配方的原材料
        if (!recMatch && !hasInputs && cm.hasMaidRecs(cookBeBase)) {
            if (!cookStateMatch) {
                List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
                findFluidResult = cm.getItem(itemStack -> {
                    return ItemStackUtil.isItem(activeItemStacks, itemStack);
                });
                if (!findFluidResult.isFail()) {
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
        if (recMatch && !cookStateMatch && !hasEnoughFluid) {
            if (!findFluidResult.isFail()) {
                List<ItemStack> activeItemStacks = cookBeBase.getActiveItems();
                findFluidResult = cm.getItem(itemStack -> {
                    return ItemStackUtil.isItem(activeItemStacks, itemStack);
                });
            }

            // todo check
            if (!findFluidResult.isFail()) {
                ItemStack fluid = findFluidResult.queryItemStack(64);
                cookBeBase.useItem(fluid, () -> {
                    return cookBeBase.hasFluid();
                }, inputInv);
                findFluidResult.backItemStack(fluid);
                cookBeBase.markChanged();
            }
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
