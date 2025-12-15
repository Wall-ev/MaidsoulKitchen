package com.github.wallev.maidsoulkitchen.mixin.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidJoyTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaidJoyTask.class)
public class MaidJoyTaskMixin {

    @Inject(method = "findJoy", at = @At("HEAD"), cancellable = true, remap = false)
    private void tlmk$disableFindJoyBlock(ServerLevel world, EntityMaid maid, CallbackInfoReturnable<BlockPos> cir) {
        if (MemoryUtil.isPlacePicnicFoodState(maid)) {
            cir.setReturnValue(null);
        }
    }

}
