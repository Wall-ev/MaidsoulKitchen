package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.MaidStorageManager;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonSplitItemAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class EnchantCommonSplitItemAction extends CommonSplitItemAction implements IFailGuideUseActionContext {

    public static final ResourceLocation TYPE = VResourceLocation.createMod("enchant_split");

    public EnchantCommonSplitItemAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        Result result = super.start();
        if (result == Result.FAIL && toFailSteps(craftGuideStepData, craftGuideData, craftLayer)) {
            return Result.SUCCESS;
        }
        return result;
    }

    @Override
    public Result tick() {
        Result result = super.tick();
        if (result == Result.FAIL && toFailSteps(craftGuideStepData, craftGuideData, craftLayer)) {
            return Result.SUCCESS;
        }
        return result;
    }
}
