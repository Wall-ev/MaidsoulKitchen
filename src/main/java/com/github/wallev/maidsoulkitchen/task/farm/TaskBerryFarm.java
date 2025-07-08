package com.github.wallev.maidsoulkitchen.task.farm;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask;
import com.github.wallev.maidsoulkitchen.compat.patchouli.entry.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.farm.ai.MaidCompatFarmMoveTask;
import com.github.wallev.maidsoulkitchen.task.farm.ai.MaidCompatFarmPlantTask;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.BerryHandler;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;


public class TaskBerryFarm extends ICompatFarmTask<BerryHandler> {
    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, BerryHandler handler) {
        return handler != null && !BLACK_LIST.contains(cropState.getBlock()) && handler.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, BerryHandler handler) {
        if (handler != null && !BLACK_LIST.contains(cropState.getBlock())) {
            handler.harvest(maid, cropPos, cropState);
        }
    }

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) return Lists.newArrayList();
        final BoundingBox checkRange = new BoundingBox(-1, 0, -1, 1, 1, 1);
        MaidCompatFarmMoveTask<BerryHandler> maidFarmMoveTask = new MaidCompatFarmMoveTask<>(maid, this, 0.6F) {
            @Override
            public boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos) {
                for (int x = checkRange.minX(); x <= checkRange.maxX(); x++) {
                    for (int y = checkRange.minY(); y <= checkRange.maxY(); y++) {
                        for (int z = checkRange.minZ(); z <= checkRange.maxZ(); z++) {
                            if (pathFinding.canPathReach(pos.offset(x, y, z))) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        };

        MaidCompatFarmPlantTask<BerryHandler> maidFarmPlantTask = new MaidCompatFarmPlantTask<>(maid, this, maidFarmMoveTask.getCompatFarmHandler());
        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
    }

    @Override
    public double getCloseEnoughDist() {
        return 2.0;
    }

    @Override
    public BerryFruitData getDefaultData() {
        return BerryFruitData.createDefaultBerry();
    }

    @Override
    public ResourceLocation getUid() {
        return MaidsoulKitchenTask.BERRY_FARM.uid;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SWEET_BERRIES.getDefaultInstance();
    }

    @Override
    public TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.BERRY_FARM;
    }

    @Override
    public TaskDataKey<BerryFruitData> getCookDataKey() {
        return DataRegister.BERRY_FARM;
    }
}
