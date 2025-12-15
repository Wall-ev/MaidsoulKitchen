package com.github.wallev.maidsoulkitchen.task.farm.advancefarm;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Collections;

public class AgeRightClickCrop<B extends Block> extends IHarvestCrop<B> {
    public final ResourceLocation UID = VResourceLocation.ofMod("age_right_click_crop");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public boolean isFarmCrop(Block block) {
        return block instanceof IPlantable && hasAgeProperty(block.defaultBlockState());
    }

    public boolean hasAgeProperty(BlockState blockState) {
        return getAge(blockState) != null;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        IntegerProperty age = getAge(cropState);
        return age != null && isMature(cropState, age);
    }

    @Override
    public boolean harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        IntegerProperty age = getAge(cropState);
        if (age == null || !isMature(cropState, age))
            return false;

        return this.harvestWithoutTool(maid, cropPos, cropState);
    }

    public boolean isMature(BlockState blockState, IntegerProperty age) {
        return blockState.getOptionalValue(age).orElse(0) >= Collections.max(age.getPossibleValues());
    }

    @Nullable
    public IntegerProperty getAge(BlockState blockState) {
        return (IntegerProperty) blockState.getProperties().stream().filter(property -> property.getName().equals("age")).findFirst().orElse(null);
    }
}
