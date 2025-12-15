package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

//public interface IFdCookingPotGuideGenerator<R extends Recipe<C>, C extends Container, B extends BlockEntity> extends ICookingRecipeGuideGenerator<R, C> {
public interface IFdCookingPotGuideGenerator<R extends Recipe<? extends Container>, B extends BlockEntity> extends ICookingRecipeGuideGenerator<R> {

    @SuppressWarnings("unchecked")
    @Override
    default boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return isValidBlockEntity(blockEntity) && isHeated((B) blockEntity);
    }

    @Override
    default boolean isBlockValid(Level level, BlockPos pos) {
        return isValidBlockEntity(level.getBlockEntity(pos));
    }

    boolean isValidBlockEntity(BlockEntity be);

    boolean isHeated(B be);

    @Override
    default  <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    default void remainStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> remains) {
        this.remainPickupStep(pos, craftGuide, remains);
    }

    @Override
    default boolean matchResultCount() {
        return true;
    }
}
