package com.github.catbert.tlma.task.farm;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.IAddonFarmTask;
import com.github.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.github.catbert.tlma.api.task.IFakePlayerTask;
import com.github.catbert.tlma.task.ai.MaidCompatFarmPlantTask;
import com.github.catbert.tlma.task.ai.MaidCompatFruitMoveTask;
import com.github.catbert.tlma.task.farm.handler.v1.IFarmHandlerManager;
import com.github.catbert.tlma.task.farm.handler.v1.fruit.*;
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


public class TaskFruitFarm implements ICompatFarm<FruitHandler>, IFakePlayerTask, IAddonFarmTask {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "fruit_farm");

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) return Lists.newArrayList();
        MaidCompatFruitMoveTask<FruitHandler> maidFarmMoveTask = new MaidCompatFruitMoveTask<>(maid, this, 0.6F);
        MaidCompatFarmPlantTask<FruitHandler> maidFarmPlantTask = new MaidCompatFarmPlantTask<>(maid, this, maidFarmMoveTask.getCompatFarmHandler());
        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
    }

    @Override
    public IFarmHandlerManager<FruitHandler>[] getManagerHandlerValues() {
        return FruitHandlerManager.values();
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, FruitHandler handler) {
//        LOGGER.info("TaskFruitFarm cropState: " + cropState);

        return handler != null && handler.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, FruitHandler handler) {
//        LOGGER.info("TaskFruitFarm start harvestWithoutDestroy " + cropState);

        this.maidRightClick(maid, cropPos);
    }

    @Override
    public double getCloseEnoughDist() {
        return 5.0;
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return Items.APPLE.getDefaultInstance();
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }
}
