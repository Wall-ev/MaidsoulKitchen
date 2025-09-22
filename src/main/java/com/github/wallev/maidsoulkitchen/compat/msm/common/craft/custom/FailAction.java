package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.mixin.compat.maidstoragemanager.CraftLayerAccessor;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.craft.context.AbstractCraftActionContext;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

import java.util.List;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class FailAction extends AbstractCraftActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.createMod("fail_action");

    public record FailData(int start, int end) {
        public static final Codec<FailData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                Codec.INT.fieldOf("start").forGetter(FailData::start),
                Codec.INT.fieldOf("end").forGetter(FailData::end)
        ).apply(ins, FailData::new));

        public static FailData to(CompoundTag tag) {
            return CODEC.parse(NbtOps.INSTANCE, tag)
                    .result()
                    .orElseThrow();
        }

        public static CompoundTag to(FailData failData) {
            return CODEC.encodeStart(NbtOps.INSTANCE, failData)
                    .result()
                    .map(tag -> (CompoundTag) tag)
                    .orElseThrow();
        }

        public static CompoundTag to(int start, int end) {
            return to(new FailData(start, end));
        }

    }

    public static CraftGuideStepData createFailStep(BlockPos pos, int start, int end) {
        return new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                TYPE,
                FailData.to(start, end)
        );
    }

    public FailAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        CompoundTag extraData = craftGuideStepData.getExtraData();
        FailData failData = FailData.to(extraData);
        int spiltStart = failData.start;
        int spiltEnd = failData.end;

        List<CraftGuideStepData> steps = ((CraftLayerAccessor) craftLayer).msk$getSteps();
        List<CraftGuideStepData> failSteps = steps.subList(spiltStart, spiltEnd + 1).stream().toList();
        steps.removeAll(failSteps);

        return Result.FAIL;
    }

    @Override
    public Result tick() {
        return Result.FAIL;
    }

    @Override
    public void stop() {

    }
}
