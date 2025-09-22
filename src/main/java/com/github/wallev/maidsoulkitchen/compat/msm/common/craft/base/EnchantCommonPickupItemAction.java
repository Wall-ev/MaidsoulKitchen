package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonPickupItemAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

import java.util.List;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class EnchantCommonPickupItemAction extends CommonPickupItemAction implements IFailGuideUseActionContext {

    public static final ResourceLocation TYPE = VResourceLocation.createMod("enchant_pickup");

    public EnchantCommonPickupItemAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
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

    public static CraftGuideStepData createStep(BlockPos pos, List<ItemStack> outputs) {
        return new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                outputs,
                TYPE
        );
    }
}
