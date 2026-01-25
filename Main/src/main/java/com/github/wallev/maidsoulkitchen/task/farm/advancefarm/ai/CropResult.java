package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai;

import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler.IHarvestCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class CropResult {
    private ResourceLocation uid;
    private double closeEnoughDist;
    private IHarvestCrop.Result result;
    private BlockPos cropPos;
    private BlockPos toMovePos;
    private BlockPos toLookPos;

    public CropResult() {
    }

    public double closeEnoughDist() {
        return closeEnoughDist;
    }

    public void setCloseEnoughDist(double closeEnoughDist) {
        this.closeEnoughDist = closeEnoughDist;
    }

    public ResourceLocation uid() {
        return uid;
    }

    public void setUid(ResourceLocation uid) {
        this.uid = uid;
    }

    public IHarvestCrop.Result result() {
        return result;
    }

    public void setResult(IHarvestCrop.Result result) {
        this.result = result;
    }

    public BlockPos getToMovePos() {
        return toMovePos;
    }

    public void setToMovePos(BlockPos pos) {
        this.toMovePos = pos;
    }

    public BlockPos cropPos() {
        return this.cropPos;
    }

    public void setCropPos(BlockPos pos) {
        this.cropPos = pos;
        this.toLookPos = pos;
        this.toMovePos = pos;
    }

    public BlockPos getToLookPos() {
        return toLookPos;
    }

    public void setToLookPos(BlockPos toLookPos) {
        this.toLookPos = toLookPos;
    }
}
