package com.github.wallev.maidsoulkitchen.mixin.mc;

import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(value = MinecraftServer.class, priority = 1752)
public abstract class MixinMinecraftServer {

    @Shadow public abstract ServerLevel overworld();

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void kjs$endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        CompletableFuture.runAsync(() -> CookRecRecipeInitializerManager.initializerData(this.overworld()), ((MinecraftServer)(Object) this));
        int a  = 1;
    }

}
