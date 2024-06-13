package com.catbert.tlma.task.farm;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.api.task.farm.ICompatFarm;
import com.catbert.tlma.api.task.farm.ICompatFarmHandler;
import com.catbert.tlma.api.task.IFakePlayer;
import com.catbert.tlma.task.ai.brain.MaidCompatFarmMoveTask;
import com.catbert.tlma.task.ai.brain.MaidCompatFarmPlantTask;
import com.catbert.tlma.task.farm.handler.v1.berry.*;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.catbert.tlma.TLMAddon.LOGGER;

@LittleMaidExtension
public class TaskBerryFarm implements ICompatFarm<BerryHandler>, IFakePlayer {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "berries_farm");

    @Override
    public BerryHandler getCompatHandler(EntityMaid maid) {
        ICompatFarmHandler.Builder<BerryHandler> berryHandlerBuilder = new BerryHandler.Builder<>();
        berryHandlerBuilder
                .addHandler(new VanillaBerryHandler())
                .addHandler(new SimpleFarmingBerryHandler())
                .addHandler(new VineryBerryHandler())
//                .addHandler(new ConviviumBerryHandler())
                .addHandler(new CompatBerryHandler());

        return berryHandlerBuilder.build();
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, BerryHandler handler) {
        LOGGER.info("TaskBerriesFarm cropState: " + cropState);

        return handler.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, BerryHandler handler) {
        LOGGER.info("TaskBerriesFarm start harvestWithoutDestroy " + cropState);
        this.maidRightClick(maid, cropPos);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level().isClientSide) return Lists.newArrayList();
        MaidCompatFarmMoveTask<BerryHandler> maidFarmMoveTask = new MaidCompatFarmMoveTask<>(maid, this, 0.6F) {
            @Override
            public boolean checkPathReach(EntityMaid maid, BlockPos pos) {
                for (int x = -1; x <= 1; ++x) {
                    for (int z = -1; z <= 1; ++z) {
                        if (maid.canPathReach(pos.offset(x, 0, z))) {
                            return true;
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
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SWEET_BERRIES.getDefaultInstance();
    }

    @Override
    public boolean canLoaded() {
        return true;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }
}
