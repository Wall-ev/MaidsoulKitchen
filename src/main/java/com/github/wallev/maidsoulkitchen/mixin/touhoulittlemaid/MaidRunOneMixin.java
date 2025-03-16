package com.github.wallev.maidsoulkitchen.mixin.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.RunIf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RunIf.class, remap = false)
public abstract class MaidRunOneMixin<E extends LivingEntity> {
    @Inject(at = @At("HEAD"), cancellable = true, method = "checkExtraStartConditions")
    private void tlmk$tryStart(ServerLevel pLevel, E entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof EntityMaid && entity.getBrain().hasMemoryValue(MkEntities.WORK_POS.get())) {
            cir.setReturnValue(false);
        }
    }
}
