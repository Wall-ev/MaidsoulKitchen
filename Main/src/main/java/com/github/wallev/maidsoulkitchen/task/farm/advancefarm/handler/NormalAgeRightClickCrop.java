package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.CropResult;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Collections;

@IHarvestCrop.AutoCropHarvestRegister
public class NormalAgeRightClickCrop extends ISurroundingHarvestCrop {
    public final ResourceLocation UID = VResourceLocation.ofMod("age_right_click_crop");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public boolean isFarmCrop(Block block) {
        return block instanceof IPlantable && hasAgeProperty(block.defaultBlockState());
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    public boolean hasAgeProperty(BlockState blockState) {
        return getAge(blockState) != null;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
        if (BLACK_LIST.contains(cropState.getBlock()))
            return Result.FAIL;

        IntegerProperty age = getAge(cropState);
        if (age == null)
            return Result.FAIL;
        if (!isMature(cropState, age))
            return Result.PASS;

        return Result.SUCCESS;
    }

    @Override
    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
        IntegerProperty age = getAge(cropState);
        if (age == null)
            return Result.FAIL;
        if (!isMature(cropState, age))
            return Result.PASS;

        return this.harvestWithoutTool(maid, cropPos, cropState, cropResult) ? Result.SUCCESS : Result.FAIL;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
        return null;
    }

    public boolean isMature(BlockState blockState, IntegerProperty age) {
        return blockState.getOptionalValue(age).orElse(0) >= Collections.max(age.getPossibleValues());
    }

    @Nullable
    public IntegerProperty getAge(BlockState blockState) {
        return (IntegerProperty) blockState.getProperties().stream().filter(property -> property.getName().equals("age")).findFirst().orElse(null);
    }
}
