package com.github.wallev.farmsoulkitchen.compat.builder.task.ab;

import com.github.wallev.farmsoulkitchen.util.function.Consumer4;
import com.github.wallev.farmsoulkitchen.util.function.Predicate4;
import com.github.wallev.farmsoulkitchen.util.function.Predicate5;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Function5;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiPredicate;

public abstract class CustomTaskABFarmBuilder<T extends CustomTaskABFarm> extends CustomTaskABBaseBuilder<T> {
    // IFarmTask
    protected BiPredicate<IFarmTask, ItemStack> isSeed;
    protected Predicate4<IFarmTask, EntityMaid, BlockPos, BlockState> canHarvest;
    protected Consumer4<IFarmTask, EntityMaid, BlockPos, BlockState> harvest;
    protected Predicate5<IFarmTask, EntityMaid, BlockPos, BlockState, ItemStack> canPlant;
    protected Function5<IFarmTask, EntityMaid, BlockPos, BlockState, ItemStack, ItemStack> plant;
    // default
    protected double closeEnoughDist = -99;
    protected boolean checkCropPosAbove = false;

    protected CustomTaskABFarmBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskABFarmBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    // IFarmTask
    public CustomTaskABFarmBuilder<T> isSeed(BiPredicate<IFarmTask, ItemStack> isSeed) {
        this.isSeed = isSeed;
        return this;
    }

    public CustomTaskABFarmBuilder<T>  canHarvest(Predicate4<IFarmTask, EntityMaid, BlockPos, BlockState> canHarvest) {
        this.canHarvest = canHarvest;
        return this;
    }
    public CustomTaskABFarmBuilder<T>  harvest(Consumer4<IFarmTask, EntityMaid, BlockPos, BlockState> harvest) {
        this.harvest = harvest;
        return this;
    }

    public CustomTaskABFarmBuilder<T>  canPlant(Predicate5<IFarmTask, EntityMaid, BlockPos, BlockState, ItemStack> canPlant) {
        this.canPlant = canPlant;
        return this;
    }

    public CustomTaskABFarmBuilder<T>  plant(Function5<IFarmTask, EntityMaid, BlockPos, BlockState, ItemStack, ItemStack> plant) {
        this.plant = plant;
        return this;
    }
    // default
    public CustomTaskABFarmBuilder<T>  setCloseEnoughDist(double closeEnoughDist) {
        this.closeEnoughDist = closeEnoughDist;
        return this;
    }

    public CustomTaskABFarmBuilder<T>  setCheckCropPosAbove(boolean checkCropPosAbove) {
        this.checkCropPosAbove = checkCropPosAbove;
        return this;
    }
}
