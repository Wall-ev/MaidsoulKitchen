package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.util.MaidUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

// 代码检测完成
// 已测试：2025年7月19日16:15:07

/**
 * 和啤酒啦啤酒桶
 */
public class NormalCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final NormalCookRule INSTANCE = new NormalCookRule<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> NormalCookRule<B, R> getInstance() {
        return (NormalCookRule<B, R>) INSTANCE;
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

        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残余的物品: 厨具有物品 && 烹饪中枢有空的输入槽位 && 厨具内的物品不符合任一配方的材料
        if (hasInputs && hasInputAvailableSlot && !recMatch) {
            return true;
        }

        boolean matchCookState = cookBeBase.cookStateMatch();
        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 有符合配方的原材料
        if (matchCookState && !hasInputs && cm.hasMaidRecs(cookBeBase)) {
            return true;
        }

        return false;
    }

    public void cookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        boolean pickAction = false;

        IItemHandlerModifiable inputInv = cm.getInputInv();
        IItemHandlerModifiable outputInv = cm.getOutputInv();

        IMaidCookInventory cookInv = cm.getCookInv();
        boolean hasInputAvailableSlot = cookInv.hasInputAvailableSlot();
        boolean hasOutputAvailableSlot = cookInv.hasOutputAvailableSlot();


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

        boolean matchCookState = cookBeBase.cookStateMatch();
        // 置入烹饪的原材料: 厨具满足烹饪的外部条件 && 厨具内没有物品 && 有符合配方的原材料
        if (matchCookState && !hasInputs && cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            cookBeBase.insertInputs(cm.pollMaidRec(cookBeBase), itemInventory);
            cookBeBase.markChanged();
            cm.getItemInventory().markDirty();

            pickAction = true;
        }

        if (pickAction) {
            MaidUtil.pickupAction(cookBeBase);
        }
    }

}
