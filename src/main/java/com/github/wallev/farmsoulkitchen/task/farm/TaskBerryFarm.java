package com.github.wallev.farmsoulkitchen.task.farm;

import com.github.wallev.farmsoulkitchen.api.TaskBookEntryType;
import com.github.wallev.farmsoulkitchen.api.task.IAddonFarmTask;
import com.github.wallev.farmsoulkitchen.api.task.v1.farm.ICompatFarm;
import com.github.wallev.farmsoulkitchen.api.task.IFakePlayerTask;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.BerryData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.BerryFarmConfigContainer;
import com.github.wallev.farmsoulkitchen.task.TaskInfo;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCompatFarmMoveTask;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCompatFarmPlantTask;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.IFarmHandlerManager;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.berry.BerryHandler;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.berry.BerryHandlerManager;
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


public class TaskBerryFarm implements ICompatFarm<BerryHandler, BerryData>, IFakePlayerTask, IAddonFarmTask {
    @Override
    public IFarmHandlerManager<BerryHandler>[] getManagerHandlerValues() {
        return BerryHandlerManager.values();
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, BerryHandler handler) {
//        LOGGER.info("TaskBerriesFarm cropState: " + cropState);

        return handler != null && handler.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, BerryHandler handler) {
//        LOGGER.info("TaskBerriesFarm start harvestWithoutDestroy " + cropState);
        this.maidRightClick(maid, cropPos);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) return Lists.newArrayList();
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
    public BerryData getDefaultData() {
        return new BerryData();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BERRY_FARM.uid;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SWEET_BERRIES.getDefaultInstance();
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }

    @Override
    public TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.BERRY_FARM;
    }

    @Override
    public MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Berry Farm Config Container");
            }

            @Override
            public AbstractContainerMenu createMenu(int index, Inventory playerInventory, Player player) {
                return new BerryFarmConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public TaskDataKey<BerryData> getCookDataKey() {
        return RegisterData.BERRY_FARM;
    }
}
