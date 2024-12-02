package com.github.wallev.farmsoulkitchen.task.farm;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.TaskBookEntryType;
import com.github.wallev.farmsoulkitchen.api.task.IAddonFarmTask;
import com.github.wallev.farmsoulkitchen.api.task.v1.farm.ICompatFarm;
import com.github.wallev.farmsoulkitchen.api.task.IFakePlayerTask;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.FruitData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.FruitFarmConfigContainer;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCompatFarmPlantTask;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCompatFruitMoveTask;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.IFarmHandlerManager;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.fruit.FruitHandler;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.fruit.FruitHandlerManager;
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
    public static final TaskFruitFarm INSTANCE = new TaskFruitFarm();

    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "fruit_farm");

    private TaskFruitFarm() {
    }

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
        return 6.0;
    }

    @Override
    public FruitData getDefaultData() {
        return new FruitData();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
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
    public static TaskFruitFarm getInstance() {
        return INSTANCE;
    }
}
