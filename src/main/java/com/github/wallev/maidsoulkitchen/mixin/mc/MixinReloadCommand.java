package com.github.wallev.maidsoulkitchen.mixin.mc;

import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ReloadCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = ReloadCommand.class)
public class MixinReloadCommand {

    @Inject(at = @At("TAIL"), method = "reloadPacks")
    private static void reloadPacks(Collection<String> pSelectedIds, CommandSourceStack pSource, CallbackInfo ci) {
        CookRecRecipeInitializerManager.initializerData(pSource.getLevel());
        int a  = 1;
    }

}
