package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.Nullable;

public final class EntityMaidUtil {
    private EntityMaidUtil() {
    }

    public static boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    public static void setWalkAndLookTargetMemories(EntityMaid maid, BlockPos walkPos, BlockPos lookPos, float pSpeed, int pDistance) {
        maid.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, pSpeed, pDistance));
        maid.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
    }

    public static BlockPos getWorkSearchCenterPos(EntityMaid maid, @Nullable final BlockPos currentWorkPos) {
        if (maid.hasRestriction()) {
            if (currentWorkPos != null && maid.isWithinRestriction(currentWorkPos)) {
                return currentWorkPos;
            } else {
                return maid.getRestrictCenter();
            }
        } else {
            return maid.blockPosition();
        }
    }
}
