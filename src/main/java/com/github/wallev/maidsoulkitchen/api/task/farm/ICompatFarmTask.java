package com.github.wallev.maidsoulkitchen.api.task.farm;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.wallev.maidsoulkitchen.api.task.IDataTask;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.FarmData;
import com.github.wallev.maidsoulkitchen.task.farm.ai.MaidCompatFarmMoveTask;
import com.github.wallev.maidsoulkitchen.task.farm.ai.MaidCompatFarmPlantTask;
import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ICompatFarmTask<T extends ICompatFarmHandler & ICompatHandlerInfo, D extends FarmData> implements IMaidsoulKitchenTask, IDataTask<D> {
    public static Set<Block> BLACK_LIST = new HashSet<>();

    private final List<IFarmHandlerManager<T>> handlerManagers;

    public ICompatFarmTask() {
        this.handlerManagers = createHandlerManagers();
    }

    private List<IFarmHandlerManager<T>> createHandlerManagers() {
        return IFarmHandlerManager.getHandlerManagers(this.getUid());
    }

    public List<IFarmHandlerManager<T>> getHandlerManagers() {
        return handlerManagers;
    }

    /**
     * 后面用于自定义女仆过滤规则
     *
     * @param maid
     * @return
     */
    public T getCompatHandler(EntityMaid maid) {
        List<String> farmTaskRulesList = getTaskData(maid).rules();
        ICompatFarmHandler.Builder<T> iCompatFarmHandlerBuilder = new ICompatFarmHandler.Builder<>();
        for (IFarmHandlerManager<T> handler : handlerManagers) {
            T farmHandler = handler.getFarmHandler();
            ResourceLocation uid = farmHandler.getUid();
            if (!farmTaskRulesList.contains(uid.toString())) continue;
            iCompatFarmHandlerBuilder.addHandler(farmHandler);
        }
        return iCompatFarmHandlerBuilder.build();
    }

    public abstract boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, T handler);

    public abstract void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, T handler);

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        MaidCompatFarmMoveTask<T> maidFarmMoveTask = new MaidCompatFarmMoveTask<>(maid, this, 0.6F);
        MaidCompatFarmPlantTask<T> maidFarmPlantTask = new MaidCompatFarmPlantTask<>(maid, this, maidFarmMoveTask.getCompatFarmHandler());
        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
    }

    public double getCloseEnoughDist() {
        return 1.0;
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FARM.get(), 0.5f);
    }

    public abstract D getDefaultData();
}
