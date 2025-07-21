package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitPoi;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaceFoodForPicnicTask extends MaidCheckRateTask {
    @Nullable
    private EntitySit sitTmp;

    public PlaceFoodForPicnicTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (!super.checkExtraStartConditions(worldIn, maid)) {
            return false;
        }

        if (!(maid.getTask() instanceof TaskCook)) {
            return false;
        }

        ItemStack hubItem = ItemCulinaryHub.getItem(maid);
        if (hubItem.isEmpty()) {
            return false;
        }

        List<BlockPos> validOutputPoses = ItemCulinaryHub.getValidOutputPoses(hubItem, maid);
        if (validOutputPoses.isEmpty()) {
            return false;
        }

        if (!findPicnicMat(worldIn, maid).isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {

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

    private List<BlockPos> findPicnicMat(ServerLevel world, EntityMaid maid) {
        BlockPos blockPos = maid.getBrainSearchPos();
        PoiManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type.get().equals(InitPoi.HOME_MEAL_BLOCK.get()), blockPos, range, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos).filter(pos -> hasMaid(world, pos) && !hasFood(maid, pos))
                .toList();
    }

    private boolean hasMaid(ServerLevel worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityPicnicMat picnicMat) {
            for (UUID uuid : picnicMat.getSitIds()) {
                if (!uuid.equals(Util.NIL_UUID) && worldIn.getEntity(uuid) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasFood(EntityMaid maid, BlockPos pos) {
        if (maid.level.getBlockEntity(pos) instanceof TileEntityPicnicMat picnicMat) {
            ItemStackHandler handler = picnicMat.getHandler();
            for (int i = 0; i < handler.getSlots(); i++) {
                if (!handler.getStackInSlot(i).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected boolean hasRequiredMemories(EntityMaid pOwner) {
        return super.hasRequiredMemories(pOwner);
    }
}
