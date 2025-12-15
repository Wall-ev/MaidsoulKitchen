package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cutter;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.special.StoneCutterRecipeAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;

public interface ICutterGuideGenerator<R extends Recipe<? extends Container>> extends ICookingRecipeGuideGenerator<R> {

    @Override
    default void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                realItems,
                outputs,
                StoneCutterRecipeAction.TYPE,
                StoneCutterRecipeAction.Cutter.toCompoundTag(cutterRecipeLoc())
        ));
    }

    @Override
    default int getRecipeTime(R recipe) {
        return 0;
    }

    ResourceLocation cutterRecipeLoc();
}
