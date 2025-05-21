package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Optional;

public class MemoryUtil {

    public static void rememberWorkPos(EntityMaid maid, BlockPos walkPos, BlockPos lookPos, float pSpeed, int pDistance) {
        maid.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, pSpeed, pDistance));
        maid.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));

        maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(lookPos));
        maid.getBrain().setMemory(MkEntities.WORK_POS.get(), new BlockPosTracker(lookPos));
    }

    public static void rememberWorkPos(EntityMaid maid, BlockPos workPos, float pSpeed, int pDistance) {
        maid.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(workPos, pSpeed, pDistance));
        maid.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(workPos));

        maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(workPos));
        maid.getBrain().setMemory(MkEntities.WORK_POS.get(), new BlockPosTracker(workPos));
    }

    public static void eraseWorkPos(EntityMaid maid) {
        maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);

        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MkEntities.WORK_POS.get());
    }
    
    public static Optional<PositionTracker> getWorkPos(EntityMaid maid) {
        return maid.getBrain().getMemory(MkEntities.WORK_POS.get());
    }

}
