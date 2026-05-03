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
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonAttackAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

import java.util.List;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class EnchantCommonAttackAction extends CommonAttackAction implements IFailGuideUseActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.createMod("enchant_destroy");

    public EnchantCommonAttackAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    public static CraftGuideStepData createStep(BlockPos pos) {
        return new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                EnchantCommonAttackAction.TYPE
        );
    }

    public static CraftGuideStepData createStep(BlockPos pos, List<ItemStack> inputs, List<ItemStack> outputs) {
        return new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                inputs,
                outputs,
                EnchantCommonAttackAction.TYPE
        );
    }

    public static CraftGuideStepData createStep(BlockPos pos, List<ItemStack> inputs) {
        return createStep(pos, inputs, List.of());
    }

    public static CraftGuideStepData createStep(BlockPos pos, ItemStack input) {
        return createStep(pos, List.of(input));
    }

    public static CraftGuideStepData createStep(BlockPos pos, ItemStack input, ItemStack output) {
        return createStep(pos, List.of(input), List.of(output));
    }

    public static CraftGuideStepData createStep(BlockPos pos, ItemStack input, List<ItemStack> outputs) {
        return createStep(pos, List.of(input), outputs);
    }

    public static CraftGuideStepData createStep(BlockPos pos, List<ItemStack> inputs, ItemStack output) {
        return createStep(pos, inputs, List.of(output));
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
