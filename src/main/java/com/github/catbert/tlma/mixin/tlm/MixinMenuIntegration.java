package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.compat.cloth.MenuIntegration;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration.class, remap = false)
public class MixinMenuIntegration {

    @Inject(at = @At("TAIL"), method = "getConfigBuilder", cancellable = true)
    private static void getConfigBuilder$tlma(CallbackInfoReturnable<ConfigBuilder> cir) {
        ConfigBuilder tlmConfigBuilder = cir.getReturnValue();
        ConfigBuilder configBuilder = MenuIntegration.getConfigBuilder(tlmConfigBuilder, true);
        cir.setReturnValue(configBuilder);
    }
}
