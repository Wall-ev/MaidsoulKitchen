package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util;

import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonPlaceItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonSplitItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonTakeItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.FailAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.FailTakeAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.mixin.compat.maidstoragemanager.CraftLayerAccessor;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonPlaceItemAction;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonSplitItemAction;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonTakeItemAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.storage.Target;

import java.util.ArrayList;
import java.util.List;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public interface IFailGuideUseActionContext {

    List<ResourceLocation> INV_ACTION = List.of(
//            EnchantCommonTakeItemAction.TYPE,
            EnchantCommonPlaceItemAction.TYPE,
            EnchantCommonSplitItemAction.TYPE,
//            CommonTakeItemAction.TYPE,
            CommonPlaceItemAction.TYPE,
            CommonSplitItemAction.TYPE
    );

    default boolean toFailSteps(CraftGuideStepData craftGuideStepData, CraftGuideData craftGuideData, CraftLayer craftLayer) {
        List<CraftGuideStepData> failSteps = new ArrayList<>();
        List<CraftGuideStepData> takeSteps = craftGuideData.steps.stream()
                .filter(step -> INV_ACTION.contains(step.action))
                .map(step -> {
                    return new CraftGuideStepData(
                            step.storage,
                            List.of(),
                            List.of(),
                            FailTakeAction.TYPE
                    );
                })
                .toList();
        failSteps.addAll(takeSteps);

        CompoundTag extraData = craftGuideStepData.getExtraData();
        List<CraftGuideStepData> customFailSteps = FailCraftGuideStepData.toFailSteps(extraData).steps();
        failSteps.addAll(customFailSteps);

        if (!failSteps.isEmpty()) {
            List<CraftGuideStepData> steps = ((CraftLayerAccessor) craftLayer).msk$getSteps();
            int inStep = craftLayer.getStep();
            int start = inStep + 1;

            // 添加失败步骤，最后由 FailAction 剔除这些失败步骤
            List<CraftGuideStepData> allFailSteps = Lists.newArrayList(failSteps);
//            List<CraftGuideStepData> allFailSteps = Lists.newArrayList(customFailSteps);
            BlockPos pos = craftGuideStepData.getStorage().getPos();
            allFailSteps.add(FailAction.createFailStep(pos, start, start + failSteps.size()));
//            allFailSteps.add(FailAction.createFailStep(pos, start, start + customFailSteps.size()));
            steps.addAll(start, allFailSteps);

            return true;
        }
        return false;
    }

}
