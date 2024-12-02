package com.github.wallev.farmsoulkitchen.compat.builder.task.ab;

import com.github.wallev.farmsoulkitchen.util.function.Consumer4;
import com.github.wallev.farmsoulkitchen.util.function.Predicate4;
import com.github.wallev.farmsoulkitchen.util.function.Predicate5;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

public abstract class CustomTaskABFarm<T extends CustomTaskABFarmBuilder> extends CustomTaskABBase<T> implements IFarmTask {

    // IFarmTask
    protected BiPredicate<IFarmTask, ItemStack> isSeed;
    protected Predicate4<IFarmTask, EntityMaid, BlockPos, BlockState> canHarvest;
    protected Consumer4<IFarmTask, EntityMaid, BlockPos, BlockState> harvest;
    protected Predicate5<IFarmTask, EntityMaid, BlockPos, BlockState, ItemStack> canPlant;
    protected Function5<IFarmTask, EntityMaid, BlockPos, BlockState, ItemStack, ItemStack> plant;
    // default
    protected double closeEnoughDist;
    protected boolean checkCropPosAbove;

    public CustomTaskABFarm(T builder) {
        super(builder);
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return this.isSeed.test(this, stack);
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.canHarvest.test(this, maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        this.harvest.accept(this, maid, cropPos, cropState);
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return this.canPlant.test(this, maid, basePos, baseState, seed);
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return this.plant.apply(this, maid, basePos, baseState, seed);
    }

    @Override
    public double getCloseEnoughDist() {
        // closeEnoughDist默认为0
        return this.closeEnoughDist != 0 ? this.closeEnoughDist : IFarmTask.super.getCloseEnoughDist();
    }

    @Override
    public boolean checkCropPosAbove() {
        // checkCropPosAbove默认为false
        return !this.checkCropPosAbove || IFarmTask.super.checkCropPosAbove();
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(EntityMaid maid) {
        return this.getAmbientSound != null ? super.getAmbientSound(maid) : IFarmTask.super.getAmbientSound(maid);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return this.createBrainTasks != null ? super.createBrainTasks(maid) : IFarmTask.super.createBrainTasks(maid);
    }

    @Override
    public void initBuilderData(T builder) {
        super.initBuilderData(builder);
        this.isSeed = builder.isSeed;
        this.canHarvest = builder.canHarvest;
        this.harvest = builder.harvest;
        this.canPlant = builder.canPlant;
        this.plant = builder.plant;
        // default
        this.closeEnoughDist = builder.closeEnoughDist;
        this.checkCropPosAbove = builder.checkCropPosAbove;
    }
}
