package com.github.wallev.maidsoulkitchen.compat.msm.kitchenkarrot.shaker;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import io.github.tt432.kitchenkarrot.registries.ModSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;


public class PlayShakeSoundAction extends EnchantCommonUseAction implements IFailGuideUseActionContext {

    public static final ResourceLocation TYPE = VResourceLocation.ofMod("place_shake_sound");

    public PlayShakeSoundAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        Result result = thisStart();
        maid.playSound(ModSoundEvents.SHAKER_OPEN.get(), 0.5F,
                maid.level.random.nextFloat() * 0.1F + 0.9F);
        maid.swing(InteractionHand.MAIN_HAND);

        if (result == Result.FAIL && toFailSteps(craftGuideStepData, craftGuideData, craftLayer)) {
            return Result.SUCCESS;
        }
        return result;
    }

    @Override
    public Result tick() {
        return Result.SUCCESS;
    }

    @Override
    public void stop() {
        this.thisStop();
    }

}
