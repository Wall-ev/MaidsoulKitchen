package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.Arrays;
import java.util.List;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public record FailCraftGuideStepData(List<CraftGuideStepData> steps) {
    public FailCraftGuideStepData(CraftGuideStepData... steps) {
        this(Arrays.stream(steps).toList());
    }

    public FailCraftGuideStepData(List<CraftGuideStepData> steps) {
        this.steps = steps;
        this.checkAndMakeOptionalSteps();
    }

    private void checkAndMakeOptionalSteps() {
        for (CraftGuideStepData step : this.steps) {
            ActionOption<Boolean> optionalAction = ActionOption.OPTIONAL;
            if (step.actionType.options().stream().noneMatch(o -> o.equals(optionalAction))) {
                continue;
            }

            step.getOptionSelection(optionalAction)
                    .ifPresentOrElse(existOptional -> {
                        if (!existOptional) {
                            ActionOptionSet.with(optionalAction, true).applyTo(step);
                        }
                    }, () -> {
                        ActionOptionSet.with(optionalAction, true).applyTo(step);
                    });
        }
    }

    public static final Codec<FailCraftGuideStepData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            CraftGuideStepData.CODEC.listOf().fieldOf("fail_steps").forGetter(FailCraftGuideStepData::steps)
    ).apply(ins, FailCraftGuideStepData::new));

    public CompoundTag toCompoundTag() {
        return CODEC.encodeStart(NbtOps.INSTANCE, this)
                .result()
                .map(tag -> (CompoundTag) tag)
                .orElseGet(CompoundTag::new);
    }

    public static CompoundTag toCompoundTag(FailCraftGuideStepData failCraftGuideStepData) {
        return failCraftGuideStepData.toCompoundTag();
    }

    public static CompoundTag toCompoundTag(List<CraftGuideStepData> steps) {
        return toCompoundTag(new FailCraftGuideStepData(steps));
    }

    public static CompoundTag toCompoundTag(CraftGuideStepData... steps) {
        return toCompoundTag(new FailCraftGuideStepData(steps));
    }

    public static FailCraftGuideStepData toFailSteps(CompoundTag compoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, compoundTag)
                .result()
                .orElse(new FailCraftGuideStepData());
    }

    public static FailCraftGuideStepData create(List<CraftGuideStepData> steps) {
        return new FailCraftGuideStepData(steps);
    }

    public static FailCraftGuideStepData create(CraftGuideStepData... steps) {
        return new FailCraftGuideStepData(steps);
    }
}
