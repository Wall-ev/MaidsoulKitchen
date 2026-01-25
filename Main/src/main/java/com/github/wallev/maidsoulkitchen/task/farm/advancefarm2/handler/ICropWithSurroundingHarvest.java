package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public abstract class ICropWithSurroundingHarvest implements ICropHarvest {

    protected final BoundingBox checkRange;

    protected ICropWithSurroundingHarvest(BoundingBox checkRange) {
        this.checkRange = checkRange;
    }

    protected ICropWithSurroundingHarvest(int offset) {
        this.checkRange = new BoundingBox(-offset, 0, -offset, offset, offset, offset);
    }

    public ICropWithSurroundingHarvest() {
        this(1);
    }

    @Override
    public boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos, HarvestData data) {
        for (int x = checkRange.minX(); x <= checkRange.maxX(); x++) {
            for (int y = checkRange.minY(); y <= checkRange.maxY(); y++) {
                for (int z = checkRange.minZ(); z <= checkRange.maxZ(); z++) {
                    if (pathFinding.canPathReach(pos.offset(x, y, z))) {
                        data.setWalkAndLookPos(pos.offset(x, y, z), pos);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
