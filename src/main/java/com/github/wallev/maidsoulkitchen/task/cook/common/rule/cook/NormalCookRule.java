package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NormalCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final NormalCookRule INSTANCE = new NormalCookRule<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> NormalCookRule<B, R> getInstance() {
        return (NormalCookRule<B, R>) INSTANCE;
    }

    public boolean canMoveTo(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        EntityMaid maid = cm.getMaid();
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

//        MemoryUtil.eraseCurrentWorkPos(maid, cookBeBase.getPos());
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


        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残存的原材料
        if (!recMatch && hasInputs) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();
        }

        // 放入烹饪的原材料
        if (matchCookState && !recMatch && cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            cookBeBase.insertInputs(cm.pollMaidRec(cookBeBase), itemInventory);
            cookBeBase.markChanged();
            cm.getItemInventory().markDirty();;
        }
    }

}
