package com.github.wallev.maidsoulkitchen.mixin.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidHomeMealTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaidHomeMealTask.class)
public class MaidHomeMealTaskMixin {

    @Inject(method = "start(Lnet/minecraft/server/level/ServerLevel;Lcom/github/tartaricacid/touhoulittlemaid/entity/passive/EntityMaid;J)V",
            at = @At("TAIL"), remap = false)
    private void tlmk$broadcastNoneFood(ServerLevel serverLevel, EntityMaid maid, long gameTime, CallbackInfo ci) {
        this.tlmk$sendNoneFoodMessage(maid);
    }

    @Unique
    private void tlmk$sendNoneFoodMessage(EntityMaid maid) {

    }

}
