package com.github.wallev.farmsoulkitchen.api.task.v1.farm;

import com.github.wallev.farmsoulkitchen.api.ILittleMaidTask;
import com.github.wallev.farmsoulkitchen.api.task.IDataTask;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.FarmData;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCompatFarmMoveTask;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCompatFarmPlantTask;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.IFarmHandlerManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface ICompatFarm<T extends ICompatFarmHandler & IHandlerInfo, D extends FarmData> extends ILittleMaidTask, IDataTask<D> {


    IFarmHandlerManager<T>[] getManagerHandlerValues();

    /**
     * 后面用于自定义女仆过滤规则
     *
     * @param maid
     * @return
     */
    default T getCompatHandler(EntityMaid maid) {
        List<String> farmTaskRulesList = getTaskData(maid).rules();
        ICompatFarmHandler.Builder<T> iCompatFarmHandlerBuilder = new ICompatFarmHandler.Builder<>();
        for (IFarmHandlerManager<T> handler : getManagerHandlerValues()) {
            T farmHandler = handler.getFarmHandler();
            ResourceLocation uid = farmHandler.getUid();
            if (!farmTaskRulesList.contains(uid.toString())) continue;
            iCompatFarmHandlerBuilder.addHandler(farmHandler);
        }
        return iCompatFarmHandlerBuilder.build();
    }

    boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, T handler);

    void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, T handler);

    default List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidCompatFarmMoveTask<T> maidFarmMoveTask = new MaidCompatFarmMoveTask<>(maid, this, 0.6F);
        MaidCompatFarmPlantTask<T> maidFarmPlantTask = new MaidCompatFarmPlantTask<>(maid, this, maidFarmMoveTask.getCompatFarmHandler());
        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
    }

    default double getCloseEnoughDist() {
        return 1.0;
    }

    @Override
    default SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FARM.get(), 0.5f);
    }

    D getDefaultData();
}
