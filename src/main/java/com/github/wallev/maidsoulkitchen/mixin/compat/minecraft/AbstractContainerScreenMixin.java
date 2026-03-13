package com.github.wallev.maidsoulkitchen.mixin.compat.minecraft;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.network.message.RefreshMaidBrainPackage;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "onClose", at = @At("HEAD"))
    private void refreshMaidBrain(CallbackInfo ci){
        if (((AbstractContainerScreen<?>)(Object)this) instanceof AbstractMaidContainerGui<?> maidContainerGui && maidContainerGui instanceof IBackpackContainerScreen) {
            Optional.ofNullable(maidContainerGui.getMaid()).ifPresent(maid -> {
                PacketDistributor.sendToServer(new RefreshMaidBrainPackage(maid.getId()));
            });
        }
    }

}
