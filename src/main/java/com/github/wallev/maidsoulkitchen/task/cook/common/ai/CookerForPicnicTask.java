package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitPoi;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CookerForPicnicTask extends MaidCheckRateTask {
    public static final int MAX_CHECK_RATE = 20 * 60;

    public CookerForPicnicTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.setMaxCheckRate(MAX_CHECK_RATE);
    }

    private static boolean picnicMatHasFood(TileEntityPicnicMat picnicMat) {
        ItemStackHandler handler = picnicMat.getHandler();
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        return super.checkExtraStartConditions(worldIn, maid) && maid.getTask() instanceof TaskCook;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        if (!(maid.getVehicle() instanceof EntitySit sit)) {
            return;
        }

        ItemStack hudItem = ItemCulinaryHub.getItem(maid);
        if (hudItem.isEmpty()) {
            return;
        }

        BlockPos onPos = sit.getAssociatedBlockPos();
        if (!(worldIn.getBlockEntity(onPos) instanceof TileEntityPicnicMat picnicMat) || picnicMatHasFood(picnicMat)) {
            return;
        }

        List<BlockPos> outputPoses = ItemCulinaryHub.getBindPoses(hudItem).get(BagType.OUTPUT);

    }

    @Nullable
    private BlockPos findPicnicMat(ServerLevel world, EntityMaid maid) {
        BlockPos blockPos = maid.getBrainSearchPos();
        PoiManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type.get().equals(InitPoi.HOME_MEAL_BLOCK.get()), blockPos, range, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos).filter(pos -> !isOccupied(world, pos))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(maid.blockPosition()))).orElse(null);
    }

    private boolean isOccupied(ServerLevel worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityPicnicMat picnicMat) {
            for (UUID uuid : picnicMat.getSitIds()) {
                if (uuid.equals(Util.NIL_UUID)) {
                    return false;
                }
                if (worldIn.getEntity(uuid) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
