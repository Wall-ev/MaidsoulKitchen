package com.github.wallev.maidsoulkitchen.mixin.tlm;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRunOne;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
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
public abstract class MixinMaidRunOne extends RunOne<EntityMaid> {

    public MixinMaidRunOne(List<Pair<? extends BehaviorControl<? super EntityMaid>, Integer>> pEntryCondition) {
        super(pEntryCondition);
    }

    @Inject(at = @At("HEAD"), method = "tryStart(Lnet/minecraft/server/level/ServerLevel;Lcom/github/tartaricacid/touhoulittlemaid/entity/passive/EntityMaid;J)Z", cancellable = true)
    private void tryStart(ServerLevel world, EntityMaid maid, long p_212834_3_, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get()) && !maid.isBegging() && !maid.isSleeping() && !(maid.getVehicle() instanceof EntitySit) && super.tryStart(world, maid, p_212834_3_));
    }

}
