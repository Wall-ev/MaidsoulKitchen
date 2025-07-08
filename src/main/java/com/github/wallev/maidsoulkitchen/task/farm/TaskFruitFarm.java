package com.github.wallev.maidsoulkitchen.task.farm;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask;
import com.github.wallev.maidsoulkitchen.compat.patchouli.entry.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.farm.ai.MaidCompatFarmPlantTask;
import com.github.wallev.maidsoulkitchen.task.farm.ai.MaidCompatFruitMoveTask;
import com.github.wallev.maidsoulkitchen.task.farm.handler.fruit.FruitHandler;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.github.wallev.verhelper.IModInfo.LOGGER;


public class TaskFruitFarm extends ICompatFarmTask<FruitHandler> {
    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) return Lists.newArrayList();
        MaidCompatFruitMoveTask<FruitHandler> maidFarmMoveTask = new MaidCompatFruitMoveTask<>(maid, this, 0.6F);
        MaidCompatFarmPlantTask<FruitHandler> maidFarmPlantTask = new MaidCompatFarmPlantTask<>(maid, this, maidFarmMoveTask.getCompatFarmHandler());
        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, FruitHandler handler) {
        return handler != null && !BLACK_LIST.contains(cropState.getBlock()) && handler.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, FruitHandler handler) {
        InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByHand(cropPos);
        if (result == InteractionResult.PASS) {
            BLACK_LIST.add(cropState.getBlock());
            LOGGER.warn(BLACK_LIST.toString());
        }
    }

    @Override
    public double getCloseEnoughDist() {
        return 6.0;
    }

    @Override
    public BerryFruitData getDefaultData() {
        return BerryFruitData.createDefaultFruit();
    }

    @Override
    public ResourceLocation getUid() {
        return MaidsoulKitchenTask.FRUIT_FARM.uid;
    }

    @Override
    public ItemStack getIcon() {
        return Items.APPLE.getDefaultInstance();
    }

    @Override
    public TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.FRUIT_FARM;
    }

    @Override
    public TaskDataKey<BerryFruitData> getCookDataKey() {
        return DataRegister.FRUIT_FARM;
    }
}
