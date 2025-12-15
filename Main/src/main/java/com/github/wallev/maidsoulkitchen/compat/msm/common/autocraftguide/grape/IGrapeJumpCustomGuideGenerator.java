package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.grape;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.JumpAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.LimitIdleAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;

public interface IGrapeJumpCustomGuideGenerator<R> extends ICookingGuideGenerator<R> {

    @Override
    default void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 放入葡萄
        for (ItemStack realItem : realItems) {
            for (int i = 0; i < realItem.getCount(); i++) {
                craftGuide.addItemUse(pos, realItem.copyWithCount(1));
            }
        }

        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                LimitIdleAction.TYPE
        ));
        // 踩葡萄
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                JumpAction.TYPE,
                ActionOptionSet.with(JumpAction.JUMP_COUNT, true, String.valueOf(jumpTime()))
        ));

        // 取出葡萄
        ItemStack result = outputs.get(0);
        for (ItemStack container : containers) {
            for (int i = 0; i < container.getCount(); i++) {
                craftGuide.addItemUse(pos, container.copyWithCount(1), result.copyWithCount(1));
            }
        }
    }

    default int getRecipeTime(R recipe) {
        return 0;
    }

    /**
     * 跳跃次数
     * @return 跳跃次数
     */
    int jumpTime();
}
