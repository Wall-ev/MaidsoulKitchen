package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.github.wallev.maidsoulkitchen.util.MaidUtil;
import com.github.wallev.maidsoulkitchen.util.TileUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceFoodForPicnicWithRideIdleTask extends MaidCheckRateTask {
    public static final int MAX_CHECK_RATE = 20 * 15;
    @Nullable
    private EntitySit sitTmp;

    public PlaceFoodForPicnicWithRideIdleTask() {
        super(ImmutableMap.of());
        this.setMaxCheckRate(MAX_CHECK_RATE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (!super.checkExtraStartConditions(worldIn, maid)) {
            return false;
        }

        if (maid.getVehicle() instanceof EntitySit sit && maid.getTask() instanceof TaskCook) {
            this.sitTmp = sit;
            return true;
        }

        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        if (this.sitTmp == null) {
            return;
        }

        BlockPos onPos = sitTmp.getAssociatedBlockPos();
        if (!(worldIn.getBlockEntity(onPos) instanceof TileEntityPicnicMat picnicMat)) {
            return;
        }

        ItemStackHandler picnicMatHandler = picnicMat.getHandler();
        if (picnicMatHasFood(picnicMatHandler)) {
            return;
        }

        ItemStack hudItem = ItemCulinaryHub.getItem(maid);
        Map<BagType, ItemStackHandler> handlers = null;
        IItemHandlerModifiable outputHandler;
        if (hudItem.isEmpty()) {
            outputHandler = maid.getAvailableBackpackInv();
        } else {
            handlers = ItemCulinaryHub.getContainers(hudItem);
            if (handlers.isEmpty()) {
                return;
            }
            outputHandler = handlers.get(BagType.OUTPUT);
        }

        if (outputHandler != null && placeFood(outputHandler, picnicMatHandler)) {
            if (handlers != null) {
                ItemCulinaryHub.setContainer(hudItem, handlers);
            }
            MaidUtil.pickupAction(maid);
            TileUtil.makeChanged(picnicMat);
            return;
        }

        List<BlockPos> validOutputPoses = ItemCulinaryHub.getValidOutputPoses(hudItem, maid);
        if (validOutputPoses.isEmpty()) {
            return;
        }
        List<Integer> aviSlots = collectPicnicMatAviFoodSlots(picnicMatHandler);
        boolean place = false;
        placeEnd:
        for (BlockPos outputPos : validOutputPoses) {
            BlockEntity be = worldIn.getBlockEntity(outputPos);
            if (be == null) {
                continue;
            }

            IItemHandler beInv = ItemCulinaryHub.getBeInv(be);
            if (beInv == null) {
                continue;
            }

            for (int i = 0; i < beInv.getSlots(); i++) {
                ItemStack itemStack = beInv.getStackInSlot(i);
                if (!itemStack.isEmpty() && itemStack.isEdible()) {
                    if (aviSlots.isEmpty()) {
                        break placeEnd;
                    }

                    Integer aviSlot = aviSlots.get(0);
                    ItemStack copy = itemStack.copy();
                    ItemStack left = picnicMatHandler.insertItem(aviSlot, copy, false);
                    itemStack.shrink(copy.getCount() - left.getCount());
                    aviSlots.remove(0);
                    place = true;
                }
            }
        }
        if (place) {
            MaidUtil.pickupAction(maid);
            TileUtil.makeChanged(picnicMat);
        }

    }

    private List<Integer> collectPicnicMatAviFoodSlots(IItemHandlerModifiable picnicMatHandler) {
        List<Integer> aviSlots = new ArrayList<>();
        for (int i = 0; i < picnicMatHandler.getSlots(); i++) {
            ItemStack itemStack = picnicMatHandler.getStackInSlot(i);
            if (itemStack.isEmpty()) {
                aviSlots.add(i);
            }
        }
        return aviSlots;
    }

    private boolean placeFood(IItemHandlerModifiable outputHandler, IItemHandlerModifiable picnicMatHandler) {
        boolean place = false;
        for (int i = 0; i < outputHandler.getSlots(); i++) {
            ItemStack itemStack = outputHandler.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.isEdible()) {
                int slot = findAviFoodSlot(picnicMatHandler);
                if (slot != -1) {
                    place = true;
                    ItemStack copy = itemStack.copy();
                    ItemStack left = picnicMatHandler.insertItem(slot, copy, false);
                    itemStack.shrink(copy.getCount() - left.getCount());
                } else {
                    return place;
                }
            }
        }
        return place;
    }

    private int findAviFoodSlot(IItemHandlerModifiable handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack itemStack = handler.getStackInSlot(i);
            if (itemStack.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private boolean picnicMatHasFood(TileEntityPicnicMat picnicMat) {
        ItemStackHandler handler = picnicMat.getHandler();
        return picnicMatHasFood(handler);
    }

    private boolean picnicMatHasFood(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean hasRequiredMemories(EntityMaid pOwner) {
        return super.hasRequiredMemories(pOwner);
    }
}
