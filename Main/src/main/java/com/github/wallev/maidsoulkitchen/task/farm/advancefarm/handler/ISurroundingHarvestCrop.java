package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.CropResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public abstract class ISurroundingHarvestCrop extends IHarvestCrop {

    private final BoundingBox checkRange;

    public ISurroundingHarvestCrop(BoundingBox checkRange) {
        this.checkRange = checkRange;
    }

    public ISurroundingHarvestCrop(int checkRange) {
        this.checkRange = new BoundingBox(-checkRange, -checkRange, -checkRange, checkRange, checkRange, checkRange);
    }

    public ISurroundingHarvestCrop() {
        this(1);
    }

    @Override
    public boolean canPathReach(MaidPathFindingBFS pathFindingBFS, BlockPos targetPos, CropResult cropResult) {
        for (int x = checkRange.minX(); x <= checkRange.maxX(); x++) {
            for (int y = checkRange.minY(); y <= checkRange.maxY(); y++) {
                for (int z = checkRange.minZ(); z <= checkRange.maxZ(); z++) {
                    if (pathFindingBFS.canPathReach(targetPos.offset(x, y, z))) {
                        cropResult.setToMovePos(targetPos.offset(x, y, z));
                        cropResult.setToLookPos(targetPos);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
