package com.github.wallev.maidsoulkitchen.mixin.compat.kitchkarrot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import io.github.tt432.kitchenkarrot.registries.ModItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@TaskMixin(TaskInfo.MSM_KK_SHAKER)
@Mixin(EntityMaid.class)
public class EntityMaidInsertItemMixin implements IMskMixinInterface {

    @Inject(method = "canInsertItem", at = @At("HEAD"), remap = false, cancellable = true)
    private static void tlmk$canInsertItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ModItems.SHAKER.get())) {
            cir.setReturnValue(true);
        }
    }

}
