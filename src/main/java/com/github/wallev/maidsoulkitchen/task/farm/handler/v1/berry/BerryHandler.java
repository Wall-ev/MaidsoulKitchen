package com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.IHandlerInfo;
import com.github.wallev.maidsoulkitchen.entity.passive.IAddonMaid;
import com.github.wallev.maidsoulkitchen.task.farm.FarmType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class BerryHandler implements ICompatFarmHandler, IHandlerInfo {
    private static final Set<BerryHandler> berryHandlers = new HashSet<>();

    // 下一级处理者
    private BerryHandler nextHandler;

    // todo
    protected BerryHandler() {
        berryHandlers.add(this);
    }

    public static Set<BerryHandler> getBerryHandlers() {
        return berryHandlers;
    }

    @Override
    public void setNextHandler(ICompatFarmHandler nextHandler) {
        this.nextHandler = (BerryHandler) nextHandler;
    }

    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public FarmType getFarmType() {
        return FarmType.BERRY;
    }

    protected abstract ActionState processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    protected abstract boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    protected final boolean harvestWithTool(EntityMaid maid, BlockPos cropPos, BlockState cropState, Predicate<ItemStack> predicate) {
        if (this.processCanHarvest(maid, cropPos, cropState) != ActionState.DENY) {
            ItemStack toolStack = ItemsUtil.getStack(maid.getAvailableInv(true), predicate);
            if (!toolStack.isEmpty()) {
                ItemStack toolCopy = toolStack.copy();
                toolStack.setCount(0);
                IAddonMaid.interactUseOnBlockWithItem(maid, cropPos, toolCopy);

                return true;
            }
        }
        return false;
    }

    protected final boolean harvestWithoutTool(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (this.processCanHarvest(maid, cropPos, cropState) != ActionState.DENY) {
            IAddonMaid.interactUseOnBlockWithoutItem(maid, cropPos);
            return true;
        }
        return false;
    }

    public final boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        ActionState actionState = processCanHarvest(maid, cropPos, cropState);
        if (actionState == ActionState.DENY) {
            return false;
        } else if (actionState == ActionState.ALLOW) {
            return true;
        } else {
            return nextHandler != null && nextHandler.canHarvest(maid, cropPos, cropState);
        }
    }

    public final void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {

        ActionState actionState = processCanHarvest(maid, cropPos, cropState);
        if (actionState == ActionState.DENY) {
            return;
        } else if (actionState == ActionState.ALLOW) {
            this.processHarvest(maid, cropPos, cropState);
        } else if (nextHandler != null) {
            nextHandler.harvest(maid, cropPos, cropState);
        }

//        ActionState actionState = processHarvest(maid, cropPos, cropState);
//        if (actionState == ActionState.DENY) {
//            return;
//        } else if (actionState == ActionState.ALLOW) {
//            this.processHarvest(maid, cropPos, cropState);
//            return;
//        } else if (nextHandler != null) {
//            nextHandler.processHarvest(maid, cropPos, cropState);
//        }

//        if (!processHarvest(maid, cropPos, cropState) && nextHandler != null) {
//            nextHandler.harvest(maid, cropPos, cropState);
//        }
    }
}
