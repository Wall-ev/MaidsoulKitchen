package com.github.wallev.maidsoulkitchen.mixin.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityMaid.class, remap = false)
public class EntityMaidMixin {

    @Inject(at = @At("RETURN"), method = "setInSittingPose")
    private void mk$setInSittingPose$resetCookWorkState(boolean inSittingPose, CallbackInfo ci) {
        if (inSittingPose) {
            MemoryUtil.resetCookWorkState((EntityMaid) (Object) this);
        }
    }

}
