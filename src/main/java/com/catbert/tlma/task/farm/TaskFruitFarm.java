package com.catbert.tlma.task.farm;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.catbert.tlma.api.task.v1.farm.ICompatFarmHandler;
import com.catbert.tlma.api.task.IFakePlayerTask;
import com.catbert.tlma.task.ai.brain.MaidCompatFarmPlantTask;
import com.catbert.tlma.task.ai.brain.MaidCompatFruitMoveTask;
import com.catbert.tlma.task.farm.handler.v1.fruit.*;
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
public class TaskFruitFarm implements ICompatFarm<FruitHandler>, IFakePlayerTask {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "fruit_farm");

    @Override
    public FruitHandler getCompatHandler(EntityMaid maid) {
        ICompatFarmHandler.Builder<FruitHandler> fruitHandlerBuilder = new FruitHandler.Builder<>();
        fruitHandlerBuilder
                .addHandler(new SimpleFarmingFruitHandler())
                .addHandler(new FruitStackFruitHandler())
                .addHandler(new VineryFruitHandler())
//                .addHandler(new CauponaFruitHandler())
                .addHandler(new CompatFruitHandler());
        return fruitHandlerBuilder.build();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level().isClientSide) return Lists.newArrayList();
        MaidCompatFruitMoveTask<FruitHandler> maidFarmMoveTask = new MaidCompatFruitMoveTask<>(maid, this, 0.6F);
        MaidCompatFarmPlantTask<FruitHandler> maidFarmPlantTask = new MaidCompatFarmPlantTask<>(maid, this, maidFarmMoveTask.getCompatFarmHandler());
        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, FruitHandler handler) {
//        LOGGER.info("TaskFruitFarm cropState: " + cropState);

        return handler.canHarvest(maid, cropPos, cropState);
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
    public boolean canLoaded() {
//        return Mods.hasLoaded(Mods.FS, Mods.CAUPONA, Mods.SF, Mods.DV);
        return true;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }
}
