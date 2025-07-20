package com.github.wallev.maidsoulkitchen.mixin.core.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.OldDataHelper;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMaid.class)
public class EntityMaidMixin {

    @Inject(method = "readAdditionalSaveData", remap = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/TamableAnimal;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER))
    private void tlmk$parseOldData(CompoundTag compound, CallbackInfo ci) {
        OldDataHelper.transOldKitchenData(compound);
    }

}
