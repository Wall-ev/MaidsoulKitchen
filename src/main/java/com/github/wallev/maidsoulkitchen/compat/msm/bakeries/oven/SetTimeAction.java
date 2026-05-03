package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.oven;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.renyigesai.bakeries.block.oven.OvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.context.AbstractCraftActionContext;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

public class SetTimeAction extends AbstractCraftActionContext implements IFailGuideUseActionContext {
    public static final ActionOption<Boolean> OPTION_WAIT = new ActionOption<>(
            VResourceLocation.createMod("set_time"),
            new Component[]{
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.idle_second"),
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.idle_tick")
            },
            new ResourceLocation[]{
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/wait_second.png"),
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/wait_tick.png")
            },
            "",
            new ActionOption.BiConverter<>(
                    i -> i != 0, b -> b ? 1 : 0
            ),
            ActionOption.ValuePredicatorOrGetter.predicator(t -> (t.isBlank() || (StringUtils.isNumeric(t) && Integer.parseInt(t) <= 999)))
    );
    public static final ResourceLocation TYPE = VResourceLocation.createMod( "oven_set_time");


    public SetTimeAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        BlockPos pos = this.getPos();
        if (maid.level.getBlockEntity(pos) instanceof OvenBlockEntity oven) {
            int temperature = this.getTemperature();
            oven.setTemperature(oven, temperature);

            return Result.SUCCESS;
        }
        if (toFailSteps(craftGuideStepData, craftGuideData, craftLayer)) {
            return Result.SUCCESS;
        }

        return Result.FAIL;
    }

    BlockPos getPos() {
        return craftGuideStepData.storage.pos;
    }

    int getTemperature() {
        boolean u = craftGuideStepData.getOptionSelection(OPTION_WAIT).orElse(false);
        String timeStr = craftGuideStepData.getOptionValue(OPTION_WAIT);
        if (timeStr.isBlank())
            timeStr = "0";
        return Integer.parseInt(timeStr);
    }

    @Override
    public Result tick() {
        return Result.SUCCESS;
    }

    @Override
    public void stop() {

    }
}
