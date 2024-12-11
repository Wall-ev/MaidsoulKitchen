package com.github.wallev.farmsoulkitchen.api.task.v1.farm;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Collections;

public interface ICompatHandler {
    default boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (!(cropState.getBlock() instanceof IPlantable)) {
            return false;
        }

        IntegerProperty age = getAge(cropState);
        return age != null && isMature(cropState, age);
    }

    default boolean isMature(BlockState blockState, IntegerProperty age) {
        return blockState.getOptionalValue(age).orElse(0) >= Collections.max(age.getPossibleValues());
    }

    @Nullable
    default IntegerProperty getAge(BlockState blockState) {
        return (IntegerProperty) blockState.getProperties().stream().filter(property -> property.getName().equals("age")).findFirst().orElse(null);
    }
}
