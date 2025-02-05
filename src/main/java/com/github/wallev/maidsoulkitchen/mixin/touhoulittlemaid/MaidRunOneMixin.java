package com.github.wallev.maidsoulkitchen.mixin.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRunOne;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MaidRunOne.class)
public abstract class MaidRunOneMixin {

    @Inject(at = @At("HEAD"), cancellable = true, method = "tryStart(Lnet/minecraft/server/level/ServerLevel;Lcom/github/tartaricacid/touhoulittlemaid/entity/passive/EntityMaid;J)Z")
    private void tlmk$tryStart(ServerLevel pLevel, EntityMaid maid, long pGameTime, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get()));
    }

}
