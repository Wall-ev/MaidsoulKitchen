package com.github.wallev.maidsoulkitchen.compat.msm.common.util.action;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.storage.Target;

import javax.annotation.Nullable;
import java.util.Optional;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class TargetUtil {
    public static Target makeTargetNoSide(ResourceLocation id, BlockPos pos) {
        return new Target(id, pos, Optional.empty());
    }

    public static Target makeTarget(ResourceLocation id, BlockPos pos, @Nullable Direction side) {
        if (side == null) {
            return makeTargetNoSide(id, pos);
        } else {
            return new Target(id, pos, Optional.of(side));
        }
    }

    public static Target makeTargetVirtualNoSide(BlockPos clickedPos) {
        return Target.virtual(clickedPos, null);
    }

    public static Target makeTargetVirtual(ResourceLocation id, BlockPos clickedPos, Direction side) {
        return Target.virtual(clickedPos, side);
    }

}
