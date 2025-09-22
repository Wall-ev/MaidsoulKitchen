package com.github.wallev.maidsoulkitchen.mixin.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.OldDataHelper;
import io.github.tt432.kitchenkarrot.registries.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityMaid.class)
public class EntityMaidMixin {

    @Inject(method = "readAdditionalSaveData", remap = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/TamableAnimal;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER))
    private void tlmk$parseOldData(CompoundTag compound, CallbackInfo ci) {
        OldDataHelper.transOldKitchenData(compound);
    }

    @Inject(method = "canInsertItem", at = @At("HEAD"), remap = false, cancellable = true)
    private static void tlmk$canInsertItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ModItems.SHAKER.get())) {
            cir.setReturnValue(true);
        }
    }

}
