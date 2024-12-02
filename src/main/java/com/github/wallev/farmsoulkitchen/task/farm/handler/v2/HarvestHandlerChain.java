package com.github.wallev.farmsoulkitchen.task.farm.handler.v2;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class HarvestHandlerChain {
    private final List<HarvestHandler> handlers = new ArrayList<>();

    public HarvestHandlerChain() {
    }

    public HarvestHandlerChain addHandler(HarvestHandler handler) {
        handlers.add(handler);
        return this;
    }

    public boolean handleHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        for (HarvestHandler handler : handlers) {
            if (handler.handleHarvest(maid, cropPos, cropState)) {
                return true;
            }
        }
        return false;
    }

    public void handleHarvestAction(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        for (HarvestHandler handler : handlers) {
            if (handler.handleHarvest(maid, cropPos, cropState)) {
                handler.handleHarvestAction(maid, cropPos, cropState);
                return;
            }
        }
    }
}
