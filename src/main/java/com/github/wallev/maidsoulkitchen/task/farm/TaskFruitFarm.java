package com.github.wallev.maidsoulkitchen.task.farm;

import com.github.wallev.maidsoulkitchen.api.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.api.task.IAddonFarmTask;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarm;
import com.github.wallev.maidsoulkitchen.api.task.IFakePlayerTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.FruitData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.FruitFarmConfigContainer;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.ai.MaidCompatFarmPlantTask;
import com.github.wallev.maidsoulkitchen.task.ai.MaidCompatFruitMoveTask;
import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.IFarmHandlerManager;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.fruit.FruitHandler;
import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.fruit.FruitHandlerManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;


public class TaskFruitFarm implements ICompatFarm<FruitHandler, FruitData>, IFakePlayerTask, IAddonFarmTask {
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
        return handler != null && !blackList.contains(cropState.getBlock()) && handler.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, FruitHandler handler) {
//        LOGGER.info("TaskFruitFarm start harvestWithoutDestroy " + cropState);

        this.maidRightClick(maid, cropPos);
    }

    @Override
    public double getCloseEnoughDist() {
        return 6.0;
    }

    @Override
    public FruitData getDefaultData() {
        return new FruitData();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FRUIT_FARM.uid;
    }

    @Override
    public ItemStack getIcon() {
        return Items.APPLE.getDefaultInstance();
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }

    @Override
    public TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.FRUIT_FARM;
    }

    @Override
    public MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Fruit Farm Config Container");
            }

            @Override
            public AbstractContainerMenu createMenu(int index, Inventory playerInventory, Player player) {
                return new FruitFarmConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public TaskDataKey<FruitData> getCookDataKey() {
        return RegisterData.FRUIT_FARM;
    }
}
