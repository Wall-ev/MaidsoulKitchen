package com.github.wallev.maidsoulkitchen.mixin.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRunOne;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.RunOne;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = MaidRunOne.class, remap = false)
public abstract class MaidRunOneMixin extends RunOne<EntityMaid> {

    public MaidRunOneMixin(List<Pair<? extends BehaviorControl<? super EntityMaid>, Integer>> pEntryCondition) {
        super(pEntryCondition);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "tryStart(Lnet/minecraft/server/level/ServerLevel;Lcom/github/tartaricacid/touhoulittlemaid/entity/passive/EntityMaid;J)Z")
    private void tlmk$tryStart(ServerLevel pLevel, EntityMaid maid, long pGameTime, CallbackInfoReturnable<Boolean> cir) {
        if (maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get())) {
            cir.setReturnValue(false);
        }
    }

}
