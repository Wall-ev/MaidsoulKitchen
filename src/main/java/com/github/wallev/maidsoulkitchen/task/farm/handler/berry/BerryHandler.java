package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandlerInfo;
import com.github.wallev.maidsoulkitchen.task.farm.FarmType;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask.BLACK_LIST;
import static com.github.wallev.verhelper.IModInfo.LOGGER;

public abstract class BerryHandler implements ICompatFarmHandler, ICompatHandlerInfo {
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
                InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByItem(cropPos, toolStack);
                if (result == InteractionResult.PASS) {
                    BLACK_LIST.add(cropState.getBlock());
                    LOGGER.warn(BLACK_LIST.toString());
                }
                return true;
            }
        }
        return false;
    }

    protected final boolean harvestWithoutTool(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (this.processCanHarvest(maid, cropPos, cropState) != ActionState.DENY) {
            InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByHand(cropPos);
            if (result == InteractionResult.PASS) {
                BLACK_LIST.add(cropState.getBlock());
                LOGGER.warn(BLACK_LIST.toString());
            }
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
        } else if (actionState == ActionState.ALLOW) {
            this.processHarvest(maid, cropPos, cropState);
        } else if (nextHandler != null) {
            nextHandler.harvest(maid, cropPos, cropState);
        }
    }
}
