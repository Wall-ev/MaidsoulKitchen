package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class SneakCommonUseAction extends EnchantCommonUseAction {

    public static final ResourceLocation TYPE = VResourceLocation.ofMod("sneak_use");

    public static final ActionOption<Boolean> SNEAK = new ActionOption<>(
            VResourceLocation.ofMod("sneak"),
            new Component[]{
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.required"),
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.optional")
            },
            new ResourceLocation[]{
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/required.png"),
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/optional.png")
            },
            "",
            new ActionOption.BiConverter<>(value -> value == 1, value -> value ? 1 : 0),
            ActionOption.ValuePredicatorOrGetter.getter(
                    value -> value ?
                            Component.translatable("gui.maid_storage_manager.craft_guide.common.optional") :
                            Component.translatable("gui.maid_storage_manager.craft_guide.common.required")
            )
    );

    public SneakCommonUseAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        Result result = super.start();
        if (result != Result.FAIL) {
            craftGuideStepData.getOptionSelection(SNEAK).ifPresentOrElse(sneak -> {
                if (sneak) {
                    fakePlayer.setShiftKeyDown(true);
                }
            }, () -> {
                fakePlayer.setShiftKeyDown(true);
            });
        }
        return result;
    }

    @Override
    public void stop() {
        fakePlayer.setShiftKeyDown(false);
        super.stop();
    }
}
