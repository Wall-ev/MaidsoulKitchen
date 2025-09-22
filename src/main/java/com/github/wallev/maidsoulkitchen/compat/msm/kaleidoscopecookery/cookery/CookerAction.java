package com.github.wallev.maidsoulkitchen.compat.msm.kaleidoscopecookery.cookery;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.craft.context.AbstractCraftActionContext;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CookerAction extends AbstractCraftActionContext implements IFailGuideUseActionContext {

    public static final ResourceLocation TYPE = VResourceLocation.createMod("cooker");

    public record CookerTime(CookerTimeInfo cookerTime) {
        public static final Codec<CookerTime> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                CookerTimeInfo.CODEC.fieldOf("cookerTime").forGetter(CookerTime::cookerTime)
        ).apply(ins, CookerTime::new));

        public CompoundTag toCompoundTag() {
            return CODEC.encodeStart(NbtOps.INSTANCE, this)
                    .result()
                    .map(tag -> (CompoundTag) tag)
                    .orElseGet(CompoundTag::new);
        }

        public static CompoundTag toCompoundTag(int time, int stirFryCount) {
            CookerTimeInfo cookerTimeInfo = new CookerTimeInfo(time, stirFryCount);
            return new CookerTime(cookerTimeInfo).toCompoundTag();
        }

        public static CookerTime toCookerTime(CompoundTag compoundTag) {
            return CODEC.parse(NbtOps.INSTANCE, compoundTag)
                    .result()
                    .orElse(new CookerTime(new CookerTimeInfo(0, 0)));
        }
    }

    private record CookerTimeInfo(int time, int stirFryCount) {
        public static final Codec<CookerTimeInfo> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                Codec.INT.fieldOf("time").forGetter(CookerTimeInfo::time),
                Codec.INT.fieldOf("stirFryCount").forGetter(CookerTimeInfo::stirFryCount)
        ).apply(ins, CookerTimeInfo::new));
    }


    int endTick = 0;
    int startTickCount = 0;
    List<Integer> stirFryTimes = Lists.newArrayList();

    final EnchantCommonUseAction useAction;

    public CookerAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
        this.useAction = new EnchantCommonUseAction(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        Result useStart = useAction.start();
        if (useStart != Result.FAIL) {
            CompoundTag extraData = craftGuideStepData.getExtraData();
            CookerTimeInfo cookerTimeInfo = CookerTime.toCookerTime(extraData).cookerTime();
            int time = cookerTimeInfo.time + 10;
            int stirFryMinCount = cookerTimeInfo.stirFryCount;
            if (endTick == 0) {
                int tickCount = Objects.requireNonNull(maid.level.getServer()).getTickCount();
                startTickCount = tickCount;
                endTick = tickCount + time;
            }

            this.initCookerInfo(time, stirFryMinCount);
        }
        return useStart;
    }

    void initCookerInfo(int time, int stirFryMinCount) {
        Random random = new Random();
        int stirFrySpace = (time - 20) / stirFryMinCount;
        for (int tick = 0; tick < time + 20; tick++) {
            if (tick % stirFrySpace == 0) {
                stirFryTimes.add(tick);
            } else {
                if (tick % 5 == 0) {
                    int nextInt = random.nextInt(1, 10);
                    if (tick % nextInt == 0) {
                        stirFryTimes.add(tick);
                    }
                }
            }
        }
    }

    @Override
    public Result tick() {
        int tickCount = Objects.requireNonNull(maid.level.getServer()).getTickCount();
        if (tickCount < endTick) {
            int tickSpace = tickCount - startTickCount;
            if (stirFryTimes.contains(tickSpace)) {
                useAction.tick();
            }
            return Result.CONTINUE;
        }
        return Result.SUCCESS;
    }

    @Override
    public void stop() {
        useAction.stop();
        endTick = 0;
    }
}
